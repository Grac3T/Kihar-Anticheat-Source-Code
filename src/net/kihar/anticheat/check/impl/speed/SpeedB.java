package net.kihar.anticheat.check.impl.speed;

import java.util.Collection;
import java.util.UUID;
import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.AttributeModifiable;
import net.minecraft.server.v1_7_R4.AttributeModifier;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.MobEffectList;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.NumberConversions;

public class SpeedB extends MoveCheck {

	private static final UUID MOVE_SPEED = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");

	private double horizontalSpeed;
	private double blockFriction;
	private double previousHorizontalMove;

	private int blockFrictionX;
	private int blockFrictionY;
	private int blockFrictionZ;

	public SpeedB(PlayerData playerData) {
		super(playerData, "Speed Type B");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		this.horizontalSpeed = this.updateMoveSpeed();

		Player player = this.getPlayer();
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		Location to = event.getTo();
		Location from = event.getFrom();

		if (!entityPlayer.server.getAllowFlight() && !player.getAllowFlight()) {
			double dx = to.getX() - from.getX();
			double dy = to.getY() - from.getY();
			double dz = to.getZ() - from.getZ();

			double horizontalSpeed = this.horizontalSpeed;
			double blockFriction = this.blockFriction;

			boolean canSprint = true;
			boolean onGround = entityPlayer.onGround;

			if (onGround) {
				if (canSprint) {
					horizontalSpeed *= 1.3;
				}

				blockFriction *= 0.91;
				horizontalSpeed *= 0.16277136 / (blockFriction * blockFriction * blockFriction);

				if (dy > 0.0001) {
					if (canSprint) {
						horizontalSpeed += 0.2;
					}

					MobEffect jumpBoost = entityPlayer.getEffect(MobEffectList.JUMP);

					// Jump boost falses this
					if (jumpBoost == null && !entityPlayer.world.c(entityPlayer.boundingBox
							.grow(0.5, 0.29, 0.5)
							.d(0.0, 0.3, 0.0)) && dy < 0.3) {
						horizontalSpeed = 0.01;
					}
				} else if (dy == 0.0) {
					// Under block
					if (entityPlayer.world.c(entityPlayer.boundingBox
							.grow(0.5, 0.0, 0.5)
							.d(0.0, 0.5, 0.0))) {
//						horizontalSpeed += 0.2;
					}
				}
			} else {
				horizontalSpeed = canSprint ? 0.026 : 0.02;
				blockFriction = 0.91;
			}

			double offsetH = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));

			double speedup = (offsetH - this.previousHorizontalMove) / horizontalSpeed;

			double vl = this.getVl();
			if (this.moveTracker.getVelocityH() <= 0 && System.currentTimeMillis() > this.moveTracker.getLastTeleportTime() + 3500L && speedup > 1.08D) {
				if ((vl += speedup) >= 30.0D) {
					this.flag(player, String.format("SPEEDUP %.4f VL %.1f", speedup, vl));
				}
			} else {
				vl -= 0.25D;
			}
			this.setVl(vl);

			this.previousHorizontalMove = offsetH * blockFriction;

			int blockX = NumberConversions.floor(to.getX());
			int blockY = NumberConversions.floor(to.getY());
			int blockZ = NumberConversions.floor(to.getZ());

			if (blockX != this.blockFrictionX ||
					blockY != this.blockFrictionY ||
					blockZ != this.blockFrictionZ) {
				this.blockFriction =
						entityPlayer.world.getType(blockX, blockY - 1, blockZ).frictionFactor;

				this.blockFrictionX = blockX;
				this.blockFrictionY = blockY;
				this.blockFrictionZ = blockZ;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public double updateMoveSpeed() {
		AttributeModifiable attribute = (AttributeModifiable) (((CraftPlayer) this.getPlayer()).getHandle()).getAttributeInstance(GenericAttributes.d);

		double base = attribute.b();
		double value = base;

		for (AttributeModifier modifier : (Collection<AttributeModifier>) attribute.a(0)) {
			value += modifier.d();
		}

		for (AttributeModifier modifier : (Collection<AttributeModifier>) attribute.a(1)) {
			value += modifier.d() * base;
		}

		for (AttributeModifier modifier : (Collection<AttributeModifier>) attribute.a(2)) {
			if (!modifier.a().equals(MOVE_SPEED)) {
				value *= 1.0 + modifier.d();
			}
		}

		return value;
	}
}
