package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetSupergateValuesWrapper(SetSupergateValues packet) implements CustomPacketPayload {

    public SetSupergateValuesWrapper(byte configuration, BlockPos pos) {
        this(new SetSupergateValues(configuration, pos));
    }

    public static SetSupergateValuesWrapper decode(FriendlyByteBuf buffer) {
        SetSupergateValues packet = SetSupergateValues.decode(buffer);
        return new SetSupergateValuesWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        SetSupergateValues.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return SetSupergateValues.CHANNEL_ID;
    }

}
