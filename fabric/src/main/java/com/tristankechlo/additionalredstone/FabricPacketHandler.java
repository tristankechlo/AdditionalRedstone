package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.network.IPacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricPacketHandler implements IPacketHandler {

    private static final ResourceLocation CHANNEL_OSCILLATOR = new ResourceLocation(Constants.MOD_ID, "oscillator");
    private static final ResourceLocation CHANNEL_SEQUENCER = new ResourceLocation(Constants.MOD_ID, "sequencer");
    private static final ResourceLocation CHANNEL_TIMER = new ResourceLocation(Constants.MOD_ID, "timer");
    private static final ResourceLocation CHANNEL_SUPERGATE = new ResourceLocation(Constants.MOD_ID, "supergate");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL_OSCILLATOR, FabricPacketHandler::handleSetOscillatorValues);
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL_SEQUENCER, FabricPacketHandler::handleSetSequencerValues);
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL_TIMER, FabricPacketHandler::handleSetTimerValues);
        ServerPlayNetworking.registerGlobalReceiver(CHANNEL_SUPERGATE, FabricPacketHandler::handleSetSupergateValues);
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetOscillatorValues.encode(new SetOscillatorValues(ticksOn, ticksOff, pos), buf);
        ClientPlayNetworking.send(CHANNEL_OSCILLATOR, buf);
    }

    static void handleSetOscillatorValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetOscillatorValues msg = SetOscillatorValues.decode(buf);
        server.execute(() -> SetOscillatorValues.handle(msg, player.serverLevel()));
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetSequencerValues.encode(new SetSequencerValues(interval, pos), buf);
        ClientPlayNetworking.send(CHANNEL_SEQUENCER, buf);
    }

    static void handleSetSequencerValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetSequencerValues msg = SetSequencerValues.decode(buf);
        server.execute(() -> SetSequencerValues.handle(msg, player.serverLevel()));
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetTimerValues.encode(new SetTimerValues(powerUpTime, powerDownTime, interval, pos), buf);
        ClientPlayNetworking.send(CHANNEL_TIMER, buf);
    }

    static void handleSetTimerValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetTimerValues msg = SetTimerValues.decode(buf);
        server.execute(() -> SetTimerValues.handle(msg, player.serverLevel()));
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetSupergateValues.encode(new SetSupergateValues(configuration, pos), buf);
        ClientPlayNetworking.send(CHANNEL_SUPERGATE, buf);
    }

    static void handleSetSupergateValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetSupergateValues msg = SetSupergateValues.decode(buf);
        server.execute(() -> SetSupergateValues.handle(msg, player.serverLevel()));
    }

}
