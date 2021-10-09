package net.kihar.anticheat.check.impl.aimassist;

import net.kihar.anticheat.check.types.RotationCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.update.RotationUpdate;

public class AimAssistA extends RotationCheck {
    public AimAssistA(PlayerData playerData) {
        super(playerData, "Aim Assist Type A");

        this.setUseViolation(false);
    }

    @Override
    public void handle(RotationUpdate update) {
        float fromYaw = (update.getFromYaw() - 90) % 360F;
        float toYaw = (update.getToYaw() - 90) % 360F;

        if (fromYaw < 0F)
            fromYaw += 360F;

        if (toYaw < 0F)
            toYaw += 360F;

        double diffYaw = Math.abs(toYaw - fromYaw);

        int vl = (int) this.getVl();

        if (diffYaw > 0D) {
            if (diffYaw % 1 == 0D) {
                if ((vl += 12) > 45) {
                    this.flag(this.getPlayer(), "VL " + vl);
                }
            } else {
                vl -= 8;
            }
        } else {
            vl -= 8;
        }

        this.setVl(vl);
    }
}
