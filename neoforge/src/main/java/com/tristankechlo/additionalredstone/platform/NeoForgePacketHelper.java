package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.IPacketHelper;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValuesPacket;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValuesPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@AutoService(IPacketHelper.class)
public class NeoForgePacketHelper implements IPacketHelper {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AdditionalRedstone.MOD_ID).versioned("1.0").optional();
        registrar.playToServer(SetOscillatorValuesPacket.TYPE, SetOscillatorValuesPacket.CODEC, NeoForgePacketHelper::handlePacket);
        registrar.playToServer(SetSequencerValuesPacket.TYPE, SetSequencerValuesPacket.CODEC, NeoForgePacketHelper::handlePacket);
        registrar.playToServer(SetTimerValuesPacket.TYPE, SetTimerValuesPacket.CODEC, NeoForgePacketHelper::handlePacket);
        registrar.playToServer(SetSupergateValuesPacket.TYPE, SetSupergateValuesPacket.CODEC, NeoForgePacketHelper::handlePacket);
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        PacketDistributor.sendToServer(new SetOscillatorValuesPacket(ticksOn, ticksOff, pos));
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        PacketDistributor.sendToServer(new SetSequencerValuesPacket(interval, pos));
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        PacketDistributor.sendToServer(new SetTimerValuesPacket(powerUpTime, powerDownTime, interval, pos));
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        PacketDistributor.sendToServer(new SetSupergateValuesPacket(configuration, pos));
    }

    private static <P extends IPacketHandler> void handlePacket(P packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            packet.handle((ServerLevel) context.player().level());
        });
    }

}
