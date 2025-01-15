package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SetSupergateValues {

    private final byte configuration;
    private final BlockPos pos;

    public SetSupergateValues(byte configuration, BlockPos pos) {
        this.configuration = configuration;
        this.pos = pos;
    }

    public static void encode(SetSupergateValues msg, FriendlyByteBuf buffer) {
        buffer.writeByte(msg.configuration);
        buffer.writeBlockPos(msg.pos);
    }

    public static SetSupergateValues decode(FriendlyByteBuf buffer) {
        byte configuration = buffer.readByte();
        BlockPos pos = buffer.readBlockPos();
        return new SetSupergateValues(configuration, pos);
    }

    public static void handle(SetSupergateValues msg, ServerLevel world) {
        if (world == null || !world.hasChunkAt(msg.pos)) {
            return;
        }
        BlockEntity entity = world.getBlockEntity(msg.pos);

        if ((entity instanceof SuperGateBlockEntity supergate)) {
            supergate.setConfiguration(msg.configuration);
            world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
            world.scheduleTick(msg.pos, supergate.getBlockState().getBlock(), 1); // force block update
            supergate.setChanged();
        }
    }
}
