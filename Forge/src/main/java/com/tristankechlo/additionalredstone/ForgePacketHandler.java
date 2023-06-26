package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ForgePacketHandler implements IPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);


    public static void registerPackets() {
        INSTANCE.registerMessage(0, SetOscillatorValues.class,
                SetOscillatorValues::encode,
                SetOscillatorValues::decode,
                ForgePacketHandler::handleSetOscillatorValues);
        INSTANCE.registerMessage(1, SetSequencerValues.class,
                SetSequencerValues::encode,
                SetSequencerValues::decode,
                ForgePacketHandler::sendPacketSetSequencerValues);
        INSTANCE.registerMessage(2, SetTimerValues.class,
                SetTimerValues::encode,
                SetTimerValues::decode,
                ForgePacketHandler::handleSetTimerValues);
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        ForgePacketHandler.INSTANCE.sendToServer(new SetOscillatorValues(ticksOn, ticksOff, pos));
    }

    static void handleSetOscillatorValues(SetOscillatorValues msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }
            SetOscillatorValues.handle(msg, player.serverLevel());
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        ForgePacketHandler.INSTANCE.sendToServer(new SetSequencerValues(interval, pos));
    }

    static void sendPacketSetSequencerValues(SetSequencerValues msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }
            SetSequencerValues.handle(msg, player.serverLevel());
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        ForgePacketHandler.INSTANCE.sendToServer(new SetTimerValues(powerUpTime, powerDownTime, interval, pos));
    }

    static void handleSetTimerValues(SetTimerValues msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) {
                return;
            }
            SetTimerValues.handle(msg, player.serverLevel());
        });
        context.get().setPacketHandled(true);
    }

}
