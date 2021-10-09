package net.kihar.anticheat.check.impl.range;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class RangeB extends PacketCheck {

    public RangeB(PlayerData playerData) {
        super(playerData, "Range B");
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK
                && this.getPlayer().getGameMode() != GameMode.CREATIVE && (System.currentTimeMillis() - playerData.getMoveTracker().getLastTeleportTime()) > 5000) {
            Entity target = ((PacketPlayInUseEntity) packet).a(((CraftPlayer) getPlayer()).getHandle().getWorld()).getBukkitEntity();

            if (target == null)
                return;

            if (target instanceof Player) {
                PlayerData targetData = this.plugin.getPlayerManager().getPlayerDataMap().get(target.getUniqueId());

                if (targetData.getPlayer().getGameMode() == GameMode.CREATIVE)
                    return;

                double range = 0;

                try {
                    range = Math.sqrt(this.playerData.getHitboxes().stream().mapToDouble(hitbox -> hitbox.getDistanceSquared(targetData.getHitboxes().stream().min((a, b) -> (int)((int)a.getDistanceSquared(hitbox) - b.getDistanceSquared(hitbox))).get())).min().orElse(0.0)) + 0.075;
                } catch (NoSuchElementException e) {
                    return;
                }

                if (range > 6.5) {
                    return;
                }

                Location playerLoc = this.getPlayer().getLocation(), targetLoc = target.getLocation();

                float playerYaw = playerLoc.getYaw(), playerPitch = playerLoc.getPitch(), targetYaw = targetLoc.getYaw(), targetPitch = targetLoc.getPitch();

                double offsetX = -Math.cos(Math.toRadians(targetPitch)) * Math.sin(Math.toRadians(targetYaw)) * -Math.cos(Math.toRadians(playerPitch)) * Math.sin(Math.toRadians(playerYaw)),
                        offsetY = -Math.sin(Math.toRadians(playerPitch)) * -Math.sin(Math.toRadians(targetPitch)),
                        offsetZ = Math.cos(Math.toRadians(targetPitch)) * Math.cos(Math.toRadians(targetYaw)) * Math.cos(Math.toRadians(playerPitch)) * Math.cos(Math.toRadians(playerYaw));

                double threshold = 3.0;

                if (offsetX + offsetY + offsetZ > 0.4) {
                    threshold += 0.15;

                }

                int ping = ((CraftPlayer)this.playerData.getPlayer()).getHandle().ping;

                threshold += ((ping > 150) ? (ping / 300) : (ping / 950));

                double vl = this.getVl();

                if (range > threshold)
                    if ((vl += range - 2.25) > 3.0)
                        this.flag(this.getPlayer(), String.format("R %.4f VL %.1f", range, vl));
                    else
                        vl -= 0.01;


                this.setVl(vl);
            }
        }
    }

}