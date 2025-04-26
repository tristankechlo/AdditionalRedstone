package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public record SetTimerValuesPacket(int powerUpTime, int powerDownTime, int interval, BlockPos pos) implements CustomPacketPayload, PacketHandler {

    @SuppressWarnings("removal") // suppress forge deprecation warnings
    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "timer");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetTimerValuesPacket> CODEC = StreamCodec.of(SetTimerValuesPacket::encode, SetTimerValuesPacket::decode);
    public static final Type<SetTimerValuesPacket> TYPE = new Type<>(CHANNEL_ID);

    public static void encode(RegistryFriendlyByteBuf buffer, SetTimerValuesPacket packet) {
        buffer.writeInt(packet.powerUpTime);
        buffer.writeInt(packet.powerDownTime);
        buffer.writeInt(packet.interval);
        buffer.writeBlockPos(packet.pos);
    }

    /* wrapper for forge, since inputs are reversed there */
    public static void encode(SetTimerValuesPacket packet, RegistryFriendlyByteBuf buffer) {
        encode(buffer, packet);
    }

    public static SetTimerValuesPacket decode(RegistryFriendlyByteBuf buffer) {
        int ticksOn = buffer.readInt();
        int ticksOff = buffer.readInt();
        int interval = buffer.readInt();
        BlockPos pos = buffer.readBlockPos();
        return new SetTimerValuesPacket(ticksOn, ticksOff, interval, pos);
    }

    public void handle(ServerLevel level) {
        if (level == null || !level.hasChunkAt(this.pos)) {
            return;
        }
        BlockEntity entity = level.getBlockEntity(this.pos);

        if (entity instanceof TimerBlockEntity timer) {
            timer.setConfiguration(this.powerUpTime, this.powerDownTime, this.interval);
            level.sendBlockUpdated(this.pos, level.getBlockState(this.pos), level.getBlockState(this.pos), 3);
            timer.setChanged();
        }
    }

    @Override
    public Type<SetTimerValuesPacket> type() {
        return TYPE;
    }

}
