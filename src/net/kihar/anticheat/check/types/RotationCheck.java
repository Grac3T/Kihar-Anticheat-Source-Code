package net.kihar.anticheat.check.types;

import net.kihar.anticheat.check.AbstractCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.player.tracker.ActionTracker;
import net.kihar.anticheat.update.RotationUpdate;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate> {
    public RotationCheck(PlayerData playerData, String name) {
        super(playerData, RotationUpdate.class, name);
    }


}
