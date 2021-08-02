package com.tristankechlo.additionalredstone.network.packets;

import java.util.function.Supplier;

import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class SetOscillatorValues {

	private final int ticksOn;
	private final int ticksOff;
	private final BlockPos pos;

	public SetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
		this.ticksOn = ticksOn;
		this.ticksOff = ticksOff;
		this.pos = pos;
	}

	public static void encode(SetOscillatorValues msg, FriendlyByteBuf buffer) {
		buffer.writeInt(msg.ticksOn);
		buffer.writeInt(msg.ticksOff);
		buffer.writeBlockPos(msg.pos);
	}

	public static SetOscillatorValues decode(FriendlyByteBuf buffer) {
		int ticksOn = buffer.readInt();
		int ticksOff = buffer.readInt();
		BlockPos pos = buffer.readBlockPos();
		return new SetOscillatorValues(ticksOn, ticksOff, pos);
	}

	@SuppressWarnings("deprecation")
	public static void handle(SetOscillatorValues msg, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {

			ServerPlayer player = context.get().getSender();
			if (player == null) {
				return;
			}
			ServerLevel world = player.getLevel();
			if (world == null || !world.hasChunkAt(msg.pos)) {
				return;
			}
			BlockEntity entity = world.getBlockEntity(msg.pos);

			if (entity != null && (entity instanceof OscillatorBlockEntity)) {
				OscillatorBlockEntity oscillator = (OscillatorBlockEntity) entity;
				oscillator.setConfiguration(Math.abs(msg.ticksOn), Math.abs(msg.ticksOff));
				world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
				oscillator.setChanged();
			}

		});
		context.get().setPacketHandled(true);
	}
}
