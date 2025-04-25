package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

@SuppressWarnings("removal") // ignore here, removed in 1.21.4+
@AutoService(IPacketHandler.class)
public final class ForgePacketHandler implements IPacketHandler {

    private static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(AdditionalRedstone.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .clientAcceptedVersions(Channel.VersionTest.exact(1))
            .serverAcceptedVersions(Channel.VersionTest.exact(1))
            .simpleChannel();

    public static void registerPackets() {
        INSTANCE.messageBuilder(SetOscillatorValues.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetOscillatorValues::encode)
                .decoder(SetOscillatorValues::decode)
                .consumerMainThread(ForgePacketHandler::handleSetOscillatorValues)
                .add();
        INSTANCE.messageBuilder(SetSequencerValues.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetSequencerValues::encode)
                .decoder(SetSequencerValues::decode)
                .consumerMainThread(ForgePacketHandler::sendPacketSetSequencerValues)
                .add();
        INSTANCE.messageBuilder(SetTimerValues.class, 2, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetTimerValues::encode)
                .decoder(SetTimerValues::decode)
                .consumerMainThread(ForgePacketHandler::handleSetTimerValues)
                .add();
        INSTANCE.messageBuilder(SetSupergateValues.class, 3, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetSupergateValues::encode)
                .decoder(SetSupergateValues::decode)
                .consumerMainThread(ForgePacketHandler::handleSetSupergateValues)
                .add();
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        INSTANCE.send(new SetOscillatorValues(ticksOn, ticksOff, pos), PacketDistributor.SERVER.noArg());
    }

    static void handleSetOscillatorValues(SetOscillatorValues msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            SetOscillatorValues.handle(msg, (ServerLevel) player.level());
        });
        context.setPacketHandled(true);
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        INSTANCE.send(new SetSequencerValues(interval, pos), PacketDistributor.SERVER.noArg());
    }

    static void sendPacketSetSequencerValues(SetSequencerValues msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            SetSequencerValues.handle(msg, (ServerLevel) player.level());
        });
        context.setPacketHandled(true);
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        INSTANCE.send(new SetTimerValues(powerUpTime, powerDownTime, interval, pos), PacketDistributor.SERVER.noArg());
    }

    static void handleSetTimerValues(SetTimerValues msg, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            SetTimerValues.handle(msg, (ServerLevel) player.level());
        });
        context.setPacketHandled(true);
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        INSTANCE.send(new SetSupergateValues(configuration, pos), PacketDistributor.SERVER.noArg());
    }

    static void handleSetSupergateValues(SetSupergateValues msg, CustomPayloadEvent.Context contextSupplier) {
        contextSupplier.enqueueWork(() -> {
            ServerPlayer player = contextSupplier.getSender();
            if (player == null) {
                return;
            }
            SetSupergateValues.handle(msg, (ServerLevel) player.level());
        });
        contextSupplier.setPacketHandled(true);
    }

}
