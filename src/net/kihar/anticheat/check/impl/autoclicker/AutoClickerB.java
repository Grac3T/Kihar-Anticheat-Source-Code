package net.kihar.anticheat.check.impl.autoclicker;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.util.MathUtil;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.Bukkit;

public class AutoClickerB extends PacketCheck {

	private final Deque<Integer> recentData = new LinkedList<>();

	private int movements;

	public AutoClickerB(PlayerData playerData) {
		super(playerData, "Auto Clicker Type B");
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation) {
			if (this.movements < 10 && !this.actionTracker.isDigging() && !this.actionTracker.isPlacing()) {
				this.recentData.add(this.movements);
				if (this.recentData.size() == 50) {
					double stdDev = MathUtil.stDeviation(this.recentData);

					int vl = (int) this.getVl();
					if (stdDev < 0.48) {
						if (++vl >= 5) {
							this.flag(this.getPlayer(), String.format("STDEV %.2f CPS %.1f VL %s", stdDev,
									1000.0D / (MathUtil.average(this.recentData) * 50.0D), vl));

							if (vl > 10)
								this.ban(this.getPlayer());
						}
					} else {
						vl--;
					}

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
