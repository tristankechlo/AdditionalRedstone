package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetSequencerValuesWrapper(SetSequencerValues packet) implements CustomPacketPayload {

    public SetSequencerValuesWrapper(int interval, BlockPos pos) {
        this(new SetSequencerValues(interval, pos));
    }

    public static SetSequencerValuesWrapper decode(FriendlyByteBuf buffer) {
        SetSequencerValues packet = SetSequencerValues.decode(buffer);
        return new SetSequencerValuesWrapper(packet);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        SetSequencerValues.encode(packet, buffer);
    }

    @Override
    public ResourceLocation id() {
        return SetSequencerValues.CHANNEL_ID;
    }

}
