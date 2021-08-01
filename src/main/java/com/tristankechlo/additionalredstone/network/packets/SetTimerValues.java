package com.tristankechlo.additionalredstone.network.packets;

import java.util.function.Supplier;

import com.tristankechlo.additionalredstone.tileentity.TimerTileEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetTimerValues {

	private final int powerUpTime;
	private final int powerDownTime;
	private final int interval;
	private final BlockPos pos;

	public SetTimerValues(int ticksOn, int ticksOff, int interval, BlockPos pos) {
		this.powerUpTime = ticksOn;
		this.powerDownTime = ticksOff;
		this.interval = interval;
		this.pos = pos;
	}

	public static void encode(SetTimerValues msg, PacketBuffer buffer) {
		buffer.writeInt(msg.powerUpTime);
		buffer.writeInt(msg.powerDownTime);
		buffer.writeInt(msg.interval);
		buffer.writeBlockPos(msg.pos);
	}

	public static SetTimerValues decode(PacketBuffer buffer) {
		int ticksOn = buffer.readInt();
		int ticksOff = buffer.readInt();
		int interval = buffer.readInt();
		BlockPos pos = buffer.readBlockPos();
		return new SetTimerValues(ticksOn, ticksOff, interval, pos);
	}

	@SuppressWarnings("deprecation")
	public static void handle(SetTimerValues msg, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {

			ServerPlayerEntity player = context.get().getSender();
			if (player == null) {
				return;
			}
			ServerWorld world = player.getLevel();
			if (world == null || !world.hasChunkAt(msg.pos)) {
				return;
			}
			TileEntity entity = world.getBlockEntity(msg.pos);

			if (entity != null && (entity instanceof TimerTileEntity)) {
				TimerTileEntity timer = (TimerTileEntity) entity;
				timer.setConfiguration(msg.powerUpTime, msg.powerDownTime, msg.interval);
				world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
				timer.setChanged();
			}

		});
		context.get().setPacketHandled(true);
	}
}
