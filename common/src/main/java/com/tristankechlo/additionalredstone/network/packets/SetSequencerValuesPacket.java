package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SetSequencerValuesPacket(int interval, BlockPos pos) implements CustomPacketPayload, IPacketHandler {

    @SuppressWarnings("removal") // suppress forge deprecation warnings
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "sequencer");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSequencerValuesPacket> CODEC = StreamCodec.of(SetSequencerValuesPacket::encode, SetSequencerValuesPacket::decode);
    public static final Type<SetSequencerValuesPacket> TYPE = new Type<>(CHANNEL_ID);

    public static void encode(RegistryFriendlyByteBuf buffer, SetSequencerValuesPacket packet) {
        buffer.writeInt(packet.interval);
        buffer.writeBlockPos(packet.pos);
    }

    /* wrapper for forge, since inputs are reversed there */
    public static void encode(SetSequencerValuesPacket packet, RegistryFriendlyByteBuf buffer) {
        encode(buffer, packet);
    }

    public static SetSequencerValuesPacket decode(RegistryFriendlyByteBuf buffer) {
        int interval = buffer.readInt();
        BlockPos pos = buffer.readBlockPos();
        return new SetSequencerValuesPacket(interval, pos);
    }

    public void handle(ServerLevel level) {
        if (level == null || !level.hasChunkAt(this.pos)) {
            return;
        }
        BlockEntity entity = level.getBlockEntity(this.pos);

        if (entity instanceof SequencerBlockEntity sequencer) {
            sequencer.setConfiguration(Math.abs(this.interval));
            level.sendBlockUpdated(this.pos, level.getBlockState(this.pos), level.getBlockState(this.pos), 3);
            sequencer.setChanged();
        }
    }

    @Override
    public Type<SetSequencerValuesPacket> type() {
        return TYPE;
    }

}
