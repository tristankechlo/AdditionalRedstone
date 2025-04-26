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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

@AutoService(IPacketHelper.class)
public final class ForgePacketHelper implements IPacketHelper {

    @SuppressWarnings("removal") // suppress forge deprecation warnings
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "main");
    private static final SimpleChannel INSTANCE = ChannelBuilder.named(CHANNEL_ID)
            .networkProtocolVersion(1)
            .clientAcceptedVersions(Channel.VersionTest.exact(1))
            .serverAcceptedVersions(Channel.VersionTest.exact(1))
            .simpleChannel();

    public static void registerPackets() {
        INSTANCE.messageBuilder(SetOscillatorValuesPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetOscillatorValuesPacket::encode)
                .decoder(SetOscillatorValuesPacket::decode)
                .consumerMainThread(ForgePacketHelper::handlePacket)
                .add();
        INSTANCE.messageBuilder(SetSequencerValuesPacket.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetSequencerValuesPacket::encode)
                .decoder(SetSequencerValuesPacket::decode)
                .consumerMainThread(ForgePacketHelper::handlePacket)
                .add();
        INSTANCE.messageBuilder(SetTimerValuesPacket.class, 2, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetTimerValuesPacket::encode)
                .decoder(SetTimerValuesPacket::decode)
                .consumerMainThread(ForgePacketHelper::handlePacket)
                .add();
        INSTANCE.messageBuilder(SetSupergateValuesPacket.class, 3, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetSupergateValuesPacket::encode)
                .decoder(SetSupergateValuesPacket::decode)
                .consumerMainThread(ForgePacketHelper::handlePacket)
                .add();
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        INSTANCE.send(new SetOscillatorValuesPacket(ticksOn, ticksOff, pos), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        INSTANCE.send(new SetSequencerValuesPacket(interval, pos), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        INSTANCE.send(new SetTimerValuesPacket(powerUpTime, powerDownTime, interval, pos), PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        INSTANCE.send(new SetSupergateValuesPacket(configuration, pos), PacketDistributor.SERVER.noArg());
    }

    private static <P extends IPacketHandler> void handlePacket(P packet, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            packet.handle((ServerLevel) player.level());
        });
        context.setPacketHandled(true);
    }

}
