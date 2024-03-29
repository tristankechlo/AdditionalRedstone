package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public static void encode(SetTimerValues msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.powerUpTime);
        buffer.writeInt(msg.powerDownTime);
        buffer.writeInt(msg.interval);
        buffer.writeBlockPos(msg.pos);
    }

    public static SetTimerValues decode(FriendlyByteBuf buffer) {
        int ticksOn = buffer.readInt();
        int ticksOff = buffer.readInt();
        int interval = buffer.readInt();
        BlockPos pos = buffer.readBlockPos();
        return new SetTimerValues(ticksOn, ticksOff, interval, pos);
    }

    public static void handle(SetTimerValues msg, ServerLevel world) {
        if (world == null || !world.hasChunkAt(msg.pos)) {
            return;
        }
        BlockEntity entity = world.getBlockEntity(msg.pos);

        if (entity != null && (entity instanceof TimerBlockEntity)) {
            TimerBlockEntity timer = (TimerBlockEntity) entity;
            timer.setConfiguration(msg.powerUpTime, msg.powerDownTime, msg.interval);
            world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
            timer.setChanged();
        }
    }
}
