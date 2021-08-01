package com.tristankechlo.additionalredstone.network.packets;

import java.util.function.Supplier;

import com.tristankechlo.additionalredstone.tileentity.SequencerTileEntity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSequencerValues {

	private final int interval;
	private final BlockPos pos;

	public SetSequencerValues(int interval, BlockPos pos) {
		this.interval = interval;
		this.pos = pos;
	}

	public static void encode(SetSequencerValues msg, PacketBuffer buffer) {
		buffer.writeInt(msg.interval);
		buffer.writeBlockPos(msg.pos);
	}

	public static SetSequencerValues decode(PacketBuffer buffer) {
		int interval = buffer.readInt();
		BlockPos pos = buffer.readBlockPos();
		return new SetSequencerValues(interval, pos);
	}

	@SuppressWarnings("deprecation")
	public static void handle(SetSequencerValues msg, Supplier<NetworkEvent.Context> context) {
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

			if (entity != null && (entity instanceof SequencerTileEntity)) {
				SequencerTileEntity sequencer = (SequencerTileEntity) entity;
				sequencer.setConfiguration(Math.abs(msg.interval));
				world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
				sequencer.setChanged();
			}

		});
		context.get().setPacketHandled(true);
	}
}
