package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import com.tristankechlo.additionalredstone.network.SetOscillatorValuesWrapper;
import com.tristankechlo.additionalredstone.network.SetSequencerValuesWrapper;
import com.tristankechlo.additionalredstone.network.SetSupergateValuesWrapper;
import com.tristankechlo.additionalredstone.network.SetTimerValuesWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@AutoService(IPacketHandler.class)
public class NeoForgePacketHandler implements IPacketHandler {

    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(AdditionalRedstone.MOD_ID).versioned("1.0").optional();
        registrar.play(
                SetOscillatorValues.CHANNEL_ID,
                SetOscillatorValuesWrapper::decode,
                handler -> handler.server(NeoForgePacketHandler::handleSetOscillatorValues)
        );
        registrar.play(
                SetSequencerValues.CHANNEL_ID,
                SetSequencerValuesWrapper::decode,
                handler -> handler.server(NeoForgePacketHandler::handleSetSequencerValues)
        );
        registrar.play(
                SetTimerValues.CHANNEL_ID,
                SetTimerValuesWrapper::decode,
                handler -> handler.server(NeoForgePacketHandler::handleSetTimerValues)
        );
        registrar.play(
                SetSupergateValues.CHANNEL_ID,
                SetSupergateValuesWrapper::decode,
                handler -> handler.server(NeoForgePacketHandler::handleSetSupergateValues)
        );
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        SetOscillatorValuesWrapper wrapper = new SetOscillatorValuesWrapper(ticksOn, ticksOff, pos);
        PacketDistributor.SERVER.noArg().send(wrapper);
    }

    private static void handleSetOscillatorValues(SetOscillatorValuesWrapper wrapper, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            SetOscillatorValues.handle(wrapper.packet(), (ServerLevel) context.level().get());
        });
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        SetSequencerValuesWrapper wrapper = new SetSequencerValuesWrapper(interval, pos);
        PacketDistributor.SERVER.noArg().send(wrapper);
    }

    private static void handleSetSequencerValues(SetSequencerValuesWrapper wrapper, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            SetSequencerValues.handle(wrapper.packet(), (ServerLevel) context.level().get());
        });
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        SetTimerValuesWrapper wrapper = new SetTimerValuesWrapper(powerUpTime, powerDownTime, interval, pos);
        PacketDistributor.SERVER.noArg().send(wrapper);
    }

    private static void handleSetTimerValues(SetTimerValuesWrapper wrapper, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            SetTimerValues.handle(wrapper.packet(), (ServerLevel) context.level().get());
        });
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        SetSupergateValuesWrapper wrapper = new SetSupergateValuesWrapper(configuration, pos);
        PacketDistributor.SERVER.noArg().send(wrapper);
    }

    private static void handleSetSupergateValues(SetSupergateValuesWrapper wrapper, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            SetSupergateValues.handle(wrapper.packet(), (ServerLevel) context.level().get());
        });
    }

}
