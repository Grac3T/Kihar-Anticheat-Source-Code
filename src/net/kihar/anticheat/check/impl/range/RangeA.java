package net.kihar.anticheat.check.impl.range;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.hitbox.PlayerHitbox;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class RangeA extends PacketCheck {

	private int ticksSinceAttack;
	private WeakReference<Player> lastTarget;
	private long lastFlag;

	public RangeA(PlayerData playerData) {
		super(playerData, "Range Type A");

		this.setUseViolation(false);
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK
				&& this.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Entity target = ((PacketPlayInUseEntity) packet).a(((CraftPlayer) getPlayer()).getHandle().getWorld()).getBukkitEntity();

			if (target == null)
				return;

			if (target instanceof Player) {
				++this.ticksSinceAttack;
				this.lastTarget = new WeakReference<>((Player) target);
			}
		} else if (packet instanceof PacketPlayInFlying && this.ticksSinceAttack == 1 && this.lastTarget.get() != null) {
			PlayerData targetData =
					this.plugin.getPlayerManager().getPlayerDataMap().get(this.lastTarget.get().getUniqueId());

			PlayerHitbox hitbox = this.playerData.getHitbox();

			double range = Math.sqrt(targetData.getHitboxes().stream()
					.mapToDouble(box -> box.getDistanceSquared(hitbox))
					.min()
					.orElse(0D));

			if (range > 6.5D) {
				return;
			}

			Location playerLoc = this.getPlayer().getLocation();
			Location targetLoc = this.lastTarget.get().getLocation();

			float playerYaw = playerLoc.getYaw();
			float playerPitch = playerLoc.getPitch();

			float targetYaw = targetLoc.getYaw();
			float targetPitch = targetLoc.getPitch();

			double offsetX = -Math.cos(Math.toRadians(targetPitch)) * Math.sin(Math.toRadians(targetYaw)) *
					-Math.cos(Math.toRadians(playerPitch)) * Math.sin(Math.toRadians(playerYaw));

			double offsetY = -Math.sin(Math.toRadians(playerPitch)) * -Math.sin(Math.toRadians(targetPitch));

			double offsetZ = Math.cos(Math.toRadians(targetPitch)) * Math.cos(Math.toRadians(targetYaw)) *
					Math.cos(Math.toRadians(playerPitch)) * Math.cos(Math.toRadians(playerYaw));

			double threshold = 3.1;

			if (offsetX + offsetY + offsetZ > 0.4) {
				threshold += 0.4;
			}

			double vl = this.getVl();
			long now = System.currentTimeMillis();

			if (range > threshold) {
				if (now > this.lastFlag + 50L) {
					if ((vl += range - 2.25) > 5D) {
						this.flag(this.getPlayer(), String.format("R %.4f VL %.1f", range, vl));
					}
				}

				this.lastFlag = now;
			} else {
				vl -= 0.035;
			}

			this.setVl(vl);

			this.ticksSinceAttack = 0;
		}
	}
}
