package com.tristankechlo.additionalredstone.network.packets;

import com.tristankechlo.additionalredstone.tileentity.SupergateTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetSupergateValues {

    private final byte configuration;
    private final BlockPos pos;

    public SetSupergateValues(byte configuration, BlockPos pos) {
        this.configuration = configuration;
        this.pos = pos;
    }

    public static void encode(SetSupergateValues msg, PacketBuffer buffer) {
        buffer.writeByte(msg.configuration);
        buffer.writeBlockPos(msg.pos);
    }

    public static SetSupergateValues decode(PacketBuffer buffer) {
        byte configuration = buffer.readByte();
        BlockPos pos = buffer.readBlockPos();
        return new SetSupergateValues(configuration, pos);
    }

    public static void handle(SetSupergateValues msg, Supplier<NetworkEvent.Context> context) {
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
            if (entity instanceof SupergateTileEntity) {
                SupergateTileEntity supergate = (SupergateTileEntity) entity;
                supergate.setConfiguration(msg.configuration);
                world.sendBlockUpdated(msg.pos, world.getBlockState(msg.pos), world.getBlockState(msg.pos), 3);
                world.getBlockTicks().scheduleTick(msg.pos, supergate.getBlockState().getBlock(), 1); // force block update
                supergate.setChanged();
            }
        });
        context.get().setPacketHandled(true);
    }

}
