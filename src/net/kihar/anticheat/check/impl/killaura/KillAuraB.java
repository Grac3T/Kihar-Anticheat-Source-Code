package net.kihar.anticheat.check.impl.killaura;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.util.MathUtil;
import net.minecraft.server.v1_7_R4.*;

public class KillAuraB extends PacketCheck {

    private int movements, lastMovements;

    private int illegalTicks, legalTicks;

    public KillAuraB(PlayerData playerData) {
        super(playerData, "Kill Aura Type B");

        setUseViolation(false);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
            if (this.movements < 10 && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
                if (this.movements == this.lastMovements) {
                    ++this.illegalTicks;
                } else {
                    ++this.legalTicks;
                }

                double vl = this.getVl();

                if (this.illegalTicks + this.legalTicks == 25) {
                    if (this.illegalTicks > 22) {
                        vl += 1D + (this.illegalTicks - 22) / 6D;

                        this.flag(this.getPlayer(), String.format("T %d, VL %.2f", this.illegalTicks, vl));

                        if (vl > 4)
                            this.ban(this.getPlayer());
                    } else {
                        vl--;
                    }
                }

                this.setVl(vl);

                this.lastMovements = this.movements;
            }

            this.movements = 0;
        } else if (packet instanceof PacketPlayInFlying) {
            this.movements++;
        }
    }
}
