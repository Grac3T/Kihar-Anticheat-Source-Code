package net.kihar.anticheat.check.impl.aimassist;

import net.kihar.anticheat.check.types.RotationCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.update.RotationUpdate;

public class AimAssistB extends RotationCheck {
    public AimAssistB(PlayerData playerData) {
        super(playerData, "Aim Assist Type B");

        this.setUseViolation(false);
    }

    @Override
    public void handle(RotationUpdate update) {
        float diffYaw = Math.abs(update.getFromYaw() - update.getToYaw()) % 180F;

        int vl = (int) this.getVl();

        if (diffYaw >= 0F && diffYaw % 0.1F == 0F) {
            if ((vl += 12) > 45) {
                this.flag(this.getPlayer(), "VL " + vl);
            }
        } else {
            vl -= 8;
        }

        this.setVl(vl);
    }
}
