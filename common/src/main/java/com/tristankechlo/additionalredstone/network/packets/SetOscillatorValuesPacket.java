package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SetOscillatorValuesPacket(int ticksOn, int ticksOff, BlockPos pos) implements CustomPacketPayload, IPacketHandler {

    public static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(AdditionalRedstone.MOD_ID, "oscillator");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetOscillatorValuesPacket> CODEC = StreamCodec.of(SetOscillatorValuesPacket::encode, SetOscillatorValuesPacket::decode);
    public static final Type<SetOscillatorValuesPacket> TYPE = new Type<>(CHANNEL_ID);

    public static void encode(RegistryFriendlyByteBuf buffer, SetOscillatorValuesPacket packet) {
        buffer.writeInt(packet.ticksOn);
        buffer.writeInt(packet.ticksOff);
        buffer.writeBlockPos(packet.pos);
    }

    /* wrapper for forge, since inputs are reversed there */
    public static void encode(SetOscillatorValuesPacket packet, RegistryFriendlyByteBuf buffer) {
        encode(buffer, packet);
    }

    public static SetOscillatorValuesPacket decode(RegistryFriendlyByteBuf buffer) {
        int ticksOn = buffer.readInt();
        int ticksOff = buffer.readInt();
        BlockPos pos = buffer.readBlockPos();
        return new SetOscillatorValuesPacket(ticksOn, ticksOff, pos);
    }

    public void handle(ServerLevel level) {
        if (level == null || !level.hasChunkAt(this.pos)) {
            return;
        }
        BlockEntity entity = level.getBlockEntity(this.pos);

        if (entity instanceof OscillatorBlockEntity oscillator) {
            oscillator.setConfiguration(Math.abs(this.ticksOn), Math.abs(this.ticksOff));
            level.sendBlockUpdated(this.pos, level.getBlockState(this.pos), level.getBlockState(this.pos), 3);
            oscillator.setChanged();
        }
    }

    @Override
    public Type<SetOscillatorValuesPacket> type() {
        return TYPE;
    }

}
