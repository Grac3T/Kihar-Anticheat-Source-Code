package net.kihar.anticheat.check.impl.speed;

import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedA extends MoveCheck {

	public SpeedA(PlayerData playerData) {
		super(playerData, "Speed Type A");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		if (this.moveTracker.getVelocityH() == 0) {
			final double offsetH = Math.hypot(event.getTo().getX() - event.getFrom().getX(), event.getTo().getZ() - event.getFrom().getZ());
			int speed = 0;
			for (final PotionEffect effect : this.getPlayer().getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.SPEED)) {
					speed = effect.getAmplifier() + 1;
					break;
				}
			}
			double threshold;
			if (this.moveTracker.isOnGround()) {
				threshold = 0.34;
				if (this.moveTracker.isOnStairs()) {
					threshold = 0.45;
				} else if (this.moveTracker.isOnIce() || this.moveTracker.getMovementsSinceIce() < 40) {
					if (this.moveTracker.isUnderBlock()) {
						threshold = 1.3;
					} else {
						threshold = 0.8;
					}
				} else if (this.moveTracker.isUnderBlock() || this.moveTracker.getMovementsSinceUnderBlock() < 40) {
					threshold = 0.7;
				} else if (this.moveTracker.isOnCarpet()) {
					threshold = 0.7;
				}
				threshold += 0.06 * speed;
			} else {
				threshold = 0.36;
				if (this.moveTracker.isOnStairs()) {
					threshold = 0.45;
				} else if (this.moveTracker.isOnIce() || this.moveTracker.getMovementsSinceIce() < 40) {
					if (this.moveTracker.isUnderBlock()) {
						threshold = 1.3;
					} else {
						threshold = 0.8;
					}
				} else if (this.moveTracker.isUnderBlock() || this.moveTracker.getMovementsSinceUnderBlock() < 40) {
					threshold = 0.7;
				} else if (this.moveTracker.isOnCarpet()) {
					threshold = 0.7;
				}
				threshold += 0.02 * speed;
			}
			threshold += this.getPlayer().getWalkSpeed() > 0.2f ? this.getPlayer().getWalkSpeed() * 10.0f * 0.33f : 0.0f;

			if (offsetH > threshold && System.currentTimeMillis() > this.moveTracker.getLastTeleportTime() + 1500L) {
				if ((this.vl += offsetH / threshold) >= 30.0D) {
					this.flag(this.getPlayer(), String.format("O %.4f T %.4f VL %s", offsetH, threshold, vl));
				}

			} else {
				this.setVl(this.vl - 0.25D);
			}
		}
	}
}
