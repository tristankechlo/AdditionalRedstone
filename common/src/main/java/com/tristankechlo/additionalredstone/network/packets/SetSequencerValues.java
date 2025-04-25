package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SetSequencerValues {

    public static final ResourceLocation CHANNEL_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "sequencer");
    private final int interval;
    private final BlockPos pos;

    public SetSequencerValues(int interval, BlockPos pos) {
        this.interval = interval;
        this.pos = pos;
    }

    public static void encode(SetSequencerValues msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.interval);
        buffer.writeBlockPos(msg.pos);
    }

    public static SetSequencerValues decode(FriendlyByteBuf buffer) {
        int interval = buffer.readInt();
        BlockPos pos = buffer.readBlockPos();
        return new SetSequencerValues(interval, pos);
    }

    public static void handle(SetSequencerValues msg, ServerLevel world) {
        if (world == null || !world.hasChunkAt(msg.pos)) {
            return;
        }
        BlockEntity entity = world.getBlockEntity(msg.pos);

        if (entity instanceof SequencerBlockEntity sequencer) {
            sequencer.setConfiguration(Math.abs(msg.interval));
            world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
            sequencer.setChanged();
        }
    }

}
