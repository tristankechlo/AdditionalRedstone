package com.tristankechlo.additionalredstone.network.packets;

import java.util.function.Supplier;

import com.tristankechlo.additionalredstone.tileentity.OscillatorTileEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetOscillatorValues {

	private final int ticksOn;
	private final int ticksOff;
	private final BlockPos pos;

	public SetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
		this.ticksOn = ticksOn;
		this.ticksOff = ticksOff;
		this.pos = pos;
	}

	public static void encode(SetOscillatorValues msg, PacketBuffer buffer) {
		buffer.writeInt(msg.ticksOn);
		buffer.writeInt(msg.ticksOff);
		buffer.writeBlockPos(msg.pos);
	}

	public static SetOscillatorValues decode(PacketBuffer buffer) {
		int ticksOn = buffer.readInt();
		int ticksOff = buffer.readInt();
		BlockPos pos = buffer.readBlockPos();
		return new SetOscillatorValues(ticksOn, ticksOff, pos);
	}

	@SuppressWarnings("deprecation")
	public static void handle(SetOscillatorValues msg, Supplier<NetworkEvent.Context> context) {
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

			if (entity != null && (entity instanceof OscillatorTileEntity)) {
				OscillatorTileEntity oscillator = (OscillatorTileEntity) entity;
				oscillator.setConfiguration(Math.abs(msg.ticksOn), Math.abs(msg.ticksOff));
				world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
				oscillator.setChanged();
			}

		});
		context.get().setPacketHandled(true);
	}
}
