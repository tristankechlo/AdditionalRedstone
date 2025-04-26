package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SetSupergateValuesPacket(byte configuration, BlockPos pos) implements CustomPacketPayload, IPacketHandler {

    @SuppressWarnings("removal") // suppress forge deprecation warnings
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "supergate");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSupergateValuesPacket> CODEC = StreamCodec.of(SetSupergateValuesPacket::encode, SetSupergateValuesPacket::decode);
    public static final Type<SetSupergateValuesPacket> TYPE = new Type<>(CHANNEL_ID);

    public static void encode(RegistryFriendlyByteBuf buffer, SetSupergateValuesPacket packet) {
        buffer.writeByte(packet.configuration);
        buffer.writeBlockPos(packet.pos);
    }

    public static void encode(SetSupergateValuesPacket packet, RegistryFriendlyByteBuf buffer) {
        encode(buffer, packet);
    }

    public static SetSupergateValuesPacket decode(RegistryFriendlyByteBuf buffer) {
        byte configuration = buffer.readByte();
        BlockPos pos = buffer.readBlockPos();
        return new SetSupergateValuesPacket(configuration, pos);
    }

    public void handle(ServerLevel level) {
        if (level == null || !level.hasChunkAt(this.pos)) {
            return;
        }
        BlockEntity entity = level.getBlockEntity(this.pos);

        if (entity instanceof SuperGateBlockEntity supergate) {
            supergate.setConfiguration(this.configuration);
            level.sendBlockUpdated(this.pos, level.getBlockState(this.pos), level.getBlockState(this.pos), 3);
            level.scheduleTick(this.pos, supergate.getBlockState().getBlock(), 1); // force block update
            supergate.setChanged();
        }
    }

    @Override
    public Type<SetSupergateValuesPacket> type() {
        return TYPE;
    }

}
