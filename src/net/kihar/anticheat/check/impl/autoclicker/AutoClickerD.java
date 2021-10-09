package net.kihar.anticheat.check.impl.autoclicker;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.util.MathUtil;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.Bukkit;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerD extends PacketCheck {

    private final Deque<Integer> recentData = new LinkedList<>();

    private int movements;

    public AutoClickerD(PlayerData playerData) {
        super(playerData, "Auto Clicker Type D");
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 250) {
                    double stdDev = MathUtil.stDeviation(this.recentData);

                    double vl = this.getVl();
                    if (stdDev < 0.55) {
                        if (++vl > 4) {
                            this.flag(this.getPlayer(), String.format("STDEV %.4f CPS %.1f VL %s", stdDev, 1000.0D / ((MathUtil.average(this.recentData)) * 50.0D), vl));
                        }
                    } else {
                        vl -= 2.4;
                    }

                    this.setVl(vl);

                    this.recentData.clear();
                }
            }
            this.movements = 0;
        } else if (packet instanceof PacketPlayInFlying) {
            this.movements++;
        }
    }
}
