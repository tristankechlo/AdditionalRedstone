package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetOscillatorValuesWrapper(SetOscillatorValues packet) implements CustomPacketPayload {

    public SetOscillatorValuesWrapper(int ticksOn, int ticksOff, BlockPos pos) {
        this(new SetOscillatorValues(ticksOn, ticksOff, pos));
    }

    public static SetOscillatorValuesWrapper decode(FriendlyByteBuf buffer) {
        SetOscillatorValues packet = SetOscillatorValues.decode(buffer);
        return new SetOscillatorValuesWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        SetOscillatorValues.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return SetOscillatorValues.CHANNEL_ID;
    }

}
