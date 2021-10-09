package net.kihar.anticheat.check.impl.autoclicker;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerE extends PacketCheck {
    private final Deque<Integer> recentData = new LinkedList<>();

    private int movements;

    public AutoClickerE(PlayerData playerData) {
        super(playerData, "Auto Clicker Type E");
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 500) {
                    int outliers = Math.toIntExact(recentData.stream()
                            .mapToInt(i -> i)
                            .filter(i -> i > 3)
                            .count());

                    double vl = this.getVl();
                    if (outliers <= 2) {
                        if ((vl += 1.4) >= 3.2) {
                            this.flag(getPlayer(), String.format("O %d, VL %.1f", outliers, vl));

                            if (vl > 5D)
                                this.ban(this.getPlayer());
                        }
                    } else {
                        vl -= 0.65;
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

