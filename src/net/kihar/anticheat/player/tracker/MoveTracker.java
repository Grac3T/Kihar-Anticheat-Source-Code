package net.kihar.anticheat.player.tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kihar.anticheat.hitbox.PlayerHitbox;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.util.BlockUtil;
import net.kihar.anticheat.wrapper.impl.WrapperPacketVelocity;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_7_R4.PacketPlayOutPosition;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
@Getter
public class MoveTracker {

	private final PlayerData playerData;

	private long lastVelocity;

	private double velocityX;
	private double velocityY;
	private double velocityZ;

	private double lastGroundY;

	private int velocityH;
	private int velocityV;

	private int movementsSinceIce;
	private int movementsSinceUnderBlock;

	private boolean attackedSinceVelocity;

	private boolean wasUnderBlock;
	private boolean wasOnGround;
	private boolean wasInLiquid;
	private boolean wasInWeb;

	private boolean underBlock;
	private boolean inLiquid;
	private boolean onGround;
	private boolean onStairs;
	private boolean onCarpet;
	private boolean inWeb;
	private boolean onIce;

	private long lastTeleportTime;

	public void handleMove(PlayerData playerData, PlayerMoveEvent event) {
		this.wasOnGround = this.onGround;
		this.wasInLiquid = this.inLiquid;
		this.wasUnderBlock = this.underBlock;
		this.wasInWeb = this.inWeb;

		Location to = event.getTo();
		Location from = event.getFrom();

		this.onGround = BlockUtil.isOnGround(to, 0) || BlockUtil.isOnGround(to, 1);

		if (this.onGround) {
			this.lastGroundY = to.getY();
		}

		this.inLiquid = BlockUtil.isOnLiquid(to, 0) || BlockUtil.isOnLiquid(to, 1);
		this.inWeb = BlockUtil.isOnWeb(to, 0);
		this.onIce = BlockUtil.isOnIce(to, 1) || BlockUtil.isOnIce(to, 2);

		if (this.onIce) {
			this.movementsSinceIce = 0;
		} else {
			this.movementsSinceIce++;
		}

		this.onStairs = BlockUtil.isOnStairs(to, 0) || BlockUtil.isOnStairs(to, 1);
		this.onCarpet = BlockUtil.isOnCarpet(to, 0) || BlockUtil.isOnCarpet(to, 1);
		this.underBlock = BlockUtil.isOnGround(to, -2);

		if (this.underBlock) {
			this.movementsSinceUnderBlock = 0;
		} else {
			this.movementsSinceUnderBlock++;
		}

		if (to.getY() != from.getY() && this.velocityV > 0) {
			this.velocityV = Math.max(this.velocityV - 1, 0);
		}
		if (Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ()) > 0.0D && this.velocityH > 0) {
			this.velocityH = Math.max(this.velocityH - 1, 0);
		}

		PlayerHitbox hitbox = new PlayerHitbox(to.getX(), to.getY(), to.getZ());

		playerData.setHitbox(hitbox);
		playerData.getHitboxes().add(hitbox);

		if (playerData.getHitboxes().size() > 8) {
			playerData.getHitboxes().removeFirst();
		}
	}

	public void handleOutboundPacket(Packet packet) {
		if (packet instanceof PacketPlayOutEntityVelocity) {
			WrapperPacketVelocity velocity = new WrapperPacketVelocity((PacketPlayOutEntityVelocity) packet);

			if (velocity.getId() == this.playerData.getPlayer().getEntityId()) {
				final double x = Math.abs(velocity.getX() / 8000.0);
				final double y = velocity.getY() / 8000.0;
				final double z = Math.abs(velocity.getZ() / 8000.0);
				if (x > 0.0 || z > 0.0) {
					this.velocityH = ((int)(((x + z) / 2.0 + 2.0) * 15.0)) * 2;
				}
				if (y > 0.0) {
					this.velocityV = ((int)(Math.pow(y + 2.0, 2.0) * 5.0));
					if (this.isOnGround() && this.playerData.getPlayer().getLocation().getY() % 1.0 == 0.0) {
						this.velocityX = x;
						this.velocityY = y;
						this.velocityZ = z;
						this.lastVelocity = System.currentTimeMillis();
						this.attackedSinceVelocity = false;
					}
				}
			}
		} else if (packet instanceof PacketPlayOutPosition) {
			this.lastTeleportTime = System.currentTimeMillis();
		}
	}
}
