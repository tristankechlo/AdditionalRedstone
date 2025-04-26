package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetTimerValuesWrapper(SetTimerValues packet) implements CustomPacketPayload {

    public SetTimerValuesWrapper(int powerUpTime, int powerDownTime, int interval, BlockPos pos) {
        this(new SetTimerValues(powerUpTime, powerDownTime, interval, pos));
    }

    public static SetTimerValuesWrapper decode(FriendlyByteBuf buffer) {
        SetTimerValues packet = SetTimerValues.decode(buffer);
        return new SetTimerValuesWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        SetTimerValues.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return SetTimerValues.CHANNEL_ID;
    }

}
