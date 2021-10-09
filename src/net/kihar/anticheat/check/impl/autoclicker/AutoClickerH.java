package net.kihar.anticheat.check.impl.autoclicker;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.OptionalDouble;
import java.util.Queue;

public class AutoClickerH extends PacketCheck {

    private final Queue<Integer> flyingPackets = new LinkedList<>();
    private int currentCount;

    public AutoClickerH(PlayerData playerData) { super(playerData, "AutoClicker 7x6");
    }

    @Override
    public void handle(Packet type) {
        if (type instanceof PacketPlayInFlying)
            this.currentCount++;
        else if (type instanceof PacketPlayInArmAnimation) {
            if (playerData.getActionTracker().isDigging() || playerData.getActionTracker().isPlacing())
                return;

            if (currentCount >= 10) {
                this.currentCount = 0;
                return;
            }

            flyingPackets.add(currentCount);

            if (flyingPackets.size() >= 75) {
                this.handleFlyingPackets();
                flyingPackets.clear();
            }

            this.currentCount = 0;
        }
    }

    private void handleFlyingPackets() {
        double rangeDifference = getRangeDifference(this.flyingPackets);

        if (rangeDifference < 2) {
            this.flag(playerData.getPlayer(), String.format("rangeDiff=%s", rangeDifference));
        }

        double kurtosis = new Kurtosis().evaluate(flyingPackets.stream().mapToDouble(Number::doubleValue).toArray());

        //if (kurtosis < 1 && kurtosis > 0) {
            //this.flag(playerData.getPlayer(), String.format("kurt=%s", kurtosis));
       // }
    }

    private double getRangeDifference(Collection<? extends Number> numbers) {
        OptionalDouble minOptional = numbers.stream().mapToDouble(Number::doubleValue).min(), maxOptional = numbers.stream().mapToDouble(Number::doubleValue).max();

        if (!minOptional.isPresent() || !maxOptional.isPresent())
            return 500;

        return maxOptional.getAsDouble() - minOptional.getAsDouble();
    }
}