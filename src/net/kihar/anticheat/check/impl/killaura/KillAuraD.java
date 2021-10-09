package net.kihar.anticheat.check.impl.killaura;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;

public class KillAuraD extends PacketCheck {
    private Boolean attack;

    public KillAuraD(PlayerData playerData) {
        super(playerData, "Kill Aura Type D");

        vl -= 3D;
    }

    @Override
    public void handle(Packet packet) {
        long now = System.currentTimeMillis();
        double vl = this.vl;

        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
            if (now - playerData.getLastFlying() < 2L) {
                attack = true;
            } else {
                attack = null;
                vl -= 0.25;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            if (attack == null)
                return;

            if (now - playerData.getLastLastFlying() > 30L && now - playerData.getLastLastFlying() < 60L) {
                if ((vl += 0.5) > 1D) {
                    this.flag(this.getPlayer(), String.format("VL %.1f", vl));
                }
            } else {
                vl -= 0.25;
            }

            attack = null;
        }

        this.setVl(vl);
    }
}
