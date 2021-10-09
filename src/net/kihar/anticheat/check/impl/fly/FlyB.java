package net.kihar.anticheat.check.impl.fly;

import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyB extends MoveCheck {

	private double lastOffsetY;

	public FlyB(PlayerData playerData) {
		super(playerData, "Fly Type B");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		if (!this.moveTracker.isInLiquid() && !this.moveTracker.isOnGround()) {
			double offsetY = event.getTo().getY() - event.getFrom().getY();

			double estimation = this.lastOffsetY * 0.9800000190734863D;
			estimation -= 0.08D;

			boolean isTeleportFalseFlag = Math.abs(offsetY) == 0.0169D || Math.abs(offsetY - -0.0980000019D) < 0.005D;
			if (isTeleportFalseFlag) {
				this.vl = 0.0D;
				return;
			}

			if (Math.abs(estimation - offsetY) > 0.005D) {
				if ((this.vl += 1.5D) >= 20.0D) {
					this.flag(this.getPlayer(), String.format("Y %.4f L %.4f VL %s", offsetY, this.lastOffsetY, vl));
				}
			} else {
				this.setVl(this.vl - 1.25D);
			}
		} else {
			vl -= 10;
		}

		this.lastOffsetY = event.getTo().getY() - event.getFrom().getY();
	}
}
