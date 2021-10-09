package net.kihar.anticheat.player.tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.server.v1_7_R4.*;

@RequiredArgsConstructor
@Getter
@Setter
public class ActionTracker {

	private boolean digging;
	private boolean placing;

	private long lastAttackTime;

	public void handleInboundPacket(Packet packet) {
		if (packet instanceof PacketPlayInBlockDig) {
			switch (((PacketPlayInBlockDig) packet).g()) {
				case 0:
					this.digging = true;
					break;
				case 1:
				case 2:
					this.digging = false;
					break;
			}
		} else if (packet instanceof PacketPlayInBlockPlace) {
			this.placing = true;
		} else if (packet instanceof PacketPlayInFlying) {
			this.placing = false;
		} else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
			lastAttackTime = System.currentTimeMillis();
		}
	}

}
