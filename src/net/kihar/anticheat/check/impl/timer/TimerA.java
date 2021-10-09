package net.kihar.anticheat.check.impl.timer;

import java.util.Deque;
import java.util.LinkedList;
import net.kihar.anticheat.check.types.PacketCheck;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

public class TimerA extends PacketCheck {

	private final Deque<Long> delays = new LinkedList<Long>();
	private long lastTickTime;

	public TimerA(PlayerData playerData) {
		super(playerData, "Timer Type A");
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof PacketPlayInFlying) {
			this.delays.add(System.currentTimeMillis() - this.lastTickTime);
			if (this.delays.size() == 40) {
				double average = 0.0;
				for (final long l : this.delays) {
					average += l;
				}
				average /= this.delays.size();
				double vl = this.getVl();
				if (average < 48.9D) {
					if ((vl += 1.0D) >= 10.0D) {
						this.flag(this.getPlayer(), String.format("F %.4f AVG %.4f VL %.1f", 50.0 / average,
								average, vl));
					}
				} else {
					vl -= 0.75D;
				}
				this.setVl(vl);
				this.delays.clear();
			}
			this.lastTickTime = System.currentTimeMillis();
		}
	}
}
