package net.kihar.anticheat.check.impl.velocity;

import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.event.player.PlayerMoveEvent;

public class VelocityA extends MoveCheck {

	public VelocityA(PlayerData playerData) {
		super(playerData, "Velocity Type A");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		final double offsetY = event.getTo().getY() - event.getFrom().getY();
		if (this.moveTracker.getVelocityY() > 0.0 && this.moveTracker.isWasOnGround() && !this.moveTracker.isUnderBlock() && !this.moveTracker.isWasUnderBlock() && !this.moveTracker.isInLiquid() && !this.moveTracker.isWasInLiquid() && !this.moveTracker.isInWeb() && !this.moveTracker.isWasInWeb() && !this.moveTracker.isOnStairs() && offsetY > 0.0 && offsetY < 0.41999998688697815 && event.getFrom().getY() % 1.0 == 0.0) {
			final double ratioY = offsetY / this.moveTracker.getVelocityY();
			int vl = (int)this.getVl();
			if (ratioY < 0.99) {
				final int percent = (int)Math.round(ratioY * 100.0);
				if (vl > 6) {
					this.flag(this.getPlayer(), "P " + percent + " VL " + vl);
				}
			}
			else {
				--vl;
			}
			this.setVl(vl);
		}
	}
}
