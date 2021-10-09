package net.kihar.anticheat.check.impl.autoclicker;

import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

public class AutoClickerA extends PacketCheck {

	private int movements;
	private int swings;

	public AutoClickerA(PlayerData playerData) {
		super(playerData, "Auto Clicker Type A");
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
			this.swings++;
		} else if (packet instanceof PacketPlayInFlying) {
			if (this.swings > 0) {
				if (++this.movements == 20) {
					if (this.swings > 20) {
						this.flag(this.getPlayer(), String.format("CPS %s", this.swings));
					}

					this.movements = this.swings = 0;
				}
			}
		}
	}
}
