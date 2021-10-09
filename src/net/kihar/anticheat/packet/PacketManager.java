package net.kihar.anticheat.packet;

import lombok.RequiredArgsConstructor;
import net.kihar.anticheat.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;

@RequiredArgsConstructor
public class PacketManager extends ChannelDuplexHandler {

	private final PlayerData playerData;

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		super.write(ctx, msg, promise);

		try {
			this.playerData.handleOutboundPacket((Packet) msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);

		try {
			this.playerData.handleInboundPacket((Packet) msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
