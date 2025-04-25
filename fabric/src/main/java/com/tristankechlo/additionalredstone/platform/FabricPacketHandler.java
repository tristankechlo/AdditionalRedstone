package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@AutoService(IPacketHandler.class)
public final class FabricPacketHandler implements IPacketHandler {

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SetOscillatorValues.CHANNEL_ID, FabricPacketHandler::handleSetOscillatorValues);
        ServerPlayNetworking.registerGlobalReceiver(SetSequencerValues.CHANNEL_ID, FabricPacketHandler::handleSetSequencerValues);
        ServerPlayNetworking.registerGlobalReceiver(SetTimerValues.CHANNEL_ID, FabricPacketHandler::handleSetTimerValues);
        ServerPlayNetworking.registerGlobalReceiver(SetSupergateValues.CHANNEL_ID, FabricPacketHandler::handleSetSupergateValues);
    }

    @Override
    public void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetOscillatorValues.encode(new SetOscillatorValues(ticksOn, ticksOff, pos), buf);
        ClientPlayNetworking.send(SetOscillatorValues.CHANNEL_ID, buf);
    }

    static void handleSetOscillatorValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetOscillatorValues msg = SetOscillatorValues.decode(buf);
        server.execute(() -> SetOscillatorValues.handle(msg, (ServerLevel) player.level()));
    }

    @Override
    public void sendPacketSetSequencerValues(int interval, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetSequencerValues.encode(new SetSequencerValues(interval, pos), buf);
        ClientPlayNetworking.send(SetSequencerValues.CHANNEL_ID, buf);
    }

    static void handleSetSequencerValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetSequencerValues msg = SetSequencerValues.decode(buf);
        server.execute(() -> SetSequencerValues.handle(msg, (ServerLevel) player.level()));
    }

    @Override
    public void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetTimerValues.encode(new SetTimerValues(powerUpTime, powerDownTime, interval, pos), buf);
        ClientPlayNetworking.send(SetTimerValues.CHANNEL_ID, buf);
    }

    static void handleSetTimerValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetTimerValues msg = SetTimerValues.decode(buf);
        server.execute(() -> SetTimerValues.handle(msg, (ServerLevel) player.level()));
    }

    @Override
    public void sendPacketSetSupergateValues(byte configuration, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        SetSupergateValues.encode(new SetSupergateValues(configuration, pos), buf);
        ClientPlayNetworking.send(SetSupergateValues.CHANNEL_ID, buf);
    }

    static void handleSetSupergateValues(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        if (player == null) {
            return;
        }
        SetSupergateValues msg = SetSupergateValues.decode(buf);
        server.execute(() -> SetSupergateValues.handle(msg, (ServerLevel) player.level()));
    }

}
