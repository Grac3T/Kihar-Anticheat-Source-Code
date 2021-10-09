package net.kihar.anticheat.check.types;

import net.kihar.anticheat.check.AbstractCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.player.tracker.MoveTracker;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class MoveCheck extends AbstractCheck<PlayerMoveEvent> {

	protected final MoveTracker moveTracker = this.playerData.getMoveTracker();

	public MoveCheck(PlayerData playerData, String name) {
		super(playerData, PlayerMoveEvent.class, name);
	}
}
