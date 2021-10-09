package net.kihar.anticheat.wrapper.impl;

import net.kihar.anticheat.wrapper.ClassWrapper;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.util.io.netty.channel.Channel;

public class WrapperPacketVelocity extends ClassWrapper<PacketPlayOutEntityVelocity> {

	public WrapperPacketVelocity(PacketPlayOutEntityVelocity instance) {
		super(instance, PacketPlayOutEntityVelocity.class);
	}

	public int getId() {
		return (int) this.getField(0);
	}

	public int getX() {
		return (int) this.getField(1);
	}

	public int getY() {
		return (int) this.getField(2);
	}

	public int getZ() {
		return (int) this.getField(3);
	}
}
