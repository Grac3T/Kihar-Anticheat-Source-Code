package net.kihar.anticheat.check.impl.autoclicker;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.util.MathUtil;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerF extends PacketCheck {

    private final Deque<Integer> recentData = new LinkedList<>();

    private int movements;

    private double lastStdev;

    public AutoClickerF(PlayerData playerData) {
        super(playerData, "Auto Clicker Type F");
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 50) {
                    double stdDev = MathUtil.stDeviation(this.recentData);

                    int vl = (int) this.getVl();
                    if (stdDev < 0.7) {
                        double diffStdev = Math.abs(stdDev - lastStdev);

                        if (diffStdev < 0.025) {
                            if (++vl >= 5) {
                                this.flag(this.getPlayer(), String.format("DSTDEV %.2f CPS %.1f VL %s", diffStdev,
                                        1000.0D / (MathUtil.average(this.recentData) * 50.0D), vl));
                            }
                        }
                    } else {
                        vl--;
                    }

                    lastStdev = stdDev;

                    this.setVl((double) vl);

                    this.recentData.clear();
                }
            }
            this.movements = 0;
        } else if (packet instanceof PacketPlayInFlying) {
            this.movements++;
        }
    }
}
