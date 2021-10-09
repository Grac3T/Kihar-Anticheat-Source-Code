package net.kihar.anticheat.player.tracker;

import net.kihar.anticheat.check.types.RotationCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.update.RotationUpdate;

public class RotationTracker {
    private Float lastYaw, lastPitch;

    public void handleLook(PlayerData playerData, float yaw, float pitch) {
        if (lastYaw != null && lastPitch != null) {
            playerData.getChecks().stream()
                    .filter(RotationCheck.class::isInstance)
                    .map(RotationCheck.class::cast)
                    .forEach(c -> c.handle(new RotationUpdate(lastYaw, yaw, lastPitch, pitch)));
        }

        lastYaw = yaw;
        lastPitch = pitch;
    }
}
