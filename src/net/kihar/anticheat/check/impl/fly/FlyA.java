package net.kihar.anticheat.check.impl.fly;

import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyA extends MoveCheck {

	public FlyA(PlayerData playerData) {
		super(playerData, "Fly Type A");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		int vl = (int)this.getVl();
		if (!this.moveTracker.isInLiquid() && !this.moveTracker.isOnGround() && this.moveTracker.getVelocityV() == 0) {
			if (event.getFrom().getY() >= event.getTo().getY()) {
				return;
			}
			final double distance = event.getTo().getY() - this.moveTracker.getLastGroundY();
			double limit = 2.0;
			if (this.getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
				for (final PotionEffect effect : this.getPlayer().getActivePotionEffects()) {
					if (effect.getType().equals(PotionEffectType.JUMP)) {
						final int level = effect.getAmplifier() + 1;
						limit += Math.pow(level + 4.2, 2.0) / 16.0;
						break;
					}
				}
			}
			if (distance > limit) {
				if (++vl >= 10) {
					this.flag(this.getPlayer(), "VL " + vl);
				}
			} else {
				vl = 0;
			}
		} else {
			vl = 0;
		}
		this.setVl(vl);
	}
}
