package net.kihar.anticheat.check.impl.fly;

import net.kihar.anticheat.check.types.MoveCheck;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyC extends MoveCheck {

	public FlyC(PlayerData playerData) {
		super(playerData, "Fly Type C");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		if (event.getTo().getY() < 4D)
			return;

		if (Math.abs(event.getTo().getY() - event.getFrom().getY()) >= 2.5 && (event.getTo().getY() - event.getFrom().getY()) != 4.334) {
			this.flag(this.getPlayer(), "O " + (event.getTo().getY() - event.getFrom().getY()));
		}
	}
}
