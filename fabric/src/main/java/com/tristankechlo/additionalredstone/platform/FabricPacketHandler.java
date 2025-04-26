package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValuesPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;

@AutoService(IPacketHandler.class)
public final class FabricPacketHandler implements IPacketHandler {

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(SetOscillatorValuesPacket.TYPE, SetOscillatorValuesPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetOscillatorValuesPacket.TYPE, FabricPacketHandler::handlePacket);
        PayloadTypeRegistry.playS2C().register(SetSequencerValuesPacket.TYPE, SetSequencerValuesPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetSequencerValuesPacket.TYPE, FabricPacketHandler::handlePacket);
        PayloadTypeRegistry.playS2C().register(SetTimerValuesPacket.TYPE, SetTimerValuesPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetTimerValuesPacket.TYPE, FabricPacketHandler::handlePacket);
        PayloadTypeRegistry.playS2C().register(SetSupergateValuesPacket.TYPE, SetSupergateValuesPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SetSupergateValuesPacket.TYPE, FabricPacketHandler::handlePacket);
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        ClientPlayNetworking.send(new SetOscillatorValuesPacket(ticksOn, ticksOff, pos));
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        ClientPlayNetworking.send(new SetSequencerValuesPacket(interval, pos));
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        ClientPlayNetworking.send(new SetTimerValuesPacket(powerUpTime, powerDownTime, interval, pos));
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        ClientPlayNetworking.send(new SetSupergateValuesPacket(configuration, pos));
    }

    private static <P extends PacketHandler> void handlePacket(P packet, ServerPlayNetworking.Context context) {
        context.server().execute(() -> packet.handle(context.player().serverLevel()));
    }

}
