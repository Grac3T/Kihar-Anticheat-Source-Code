package net.kihar.anticheat.check.impl.aimassist;

import net.kihar.anticheat.check.types.RotationCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.update.RotationUpdate;

public class AimAssistC extends RotationCheck {
    private float lastYawRate;
    private float lastPitchRate;

    public AimAssistC(PlayerData playerData) {
        super(playerData, "Aim Assist Type C");

        this.setUseViolation(false);
    }

    @Override
    public void handle(RotationUpdate update) {
        if (System.currentTimeMillis() > playerData.getActionTracker().getLastAttackTime() + 10000L)
            return;

        float diffYaw = Math.abs(update.getFromYaw() - update.getToYaw());
        float diffPitch = Math.abs(update.getFromPitch() - update.getToPitch());

        float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
        float diffYawRate = Math.abs(this.lastYawRate - diffYaw);

        float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
        float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);

        int vl = (int) this.getVl();

        if (diffPitch > 0.001 && diffPitch < 0.0094 && diffPitchRate > 1F && diffYawRate > 1F && diffYaw > 3F &&
                this.lastYawRate > 1.5 && (diffPitchRatePitch > 1F || diffYawRateYaw > 1F)) {
            this.flag(this.getPlayer(), String.format("P %.3f, VL %s", diffPitch, ++vl));
        }

        this.setVl(vl);

        this.lastPitchRate = diffPitch;
        this.lastYawRate = diffYaw;
    }
}
