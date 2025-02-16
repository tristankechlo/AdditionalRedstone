package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AdditionalRedstone.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        INSTANCE.registerMessage(0, SetOscillatorValues.class, SetOscillatorValues::encode, SetOscillatorValues::decode, SetOscillatorValues::handle);
        INSTANCE.registerMessage(1, SetTimerValues.class, SetTimerValues::encode, SetTimerValues::decode, SetTimerValues::handle);
        INSTANCE.registerMessage(2, SetSequencerValues.class, SetSequencerValues::encode, SetSequencerValues::decode, SetSequencerValues::handle);
        INSTANCE.registerMessage(3, SetSupergateValues.class, SetSupergateValues::encode, SetSupergateValues::decode, SetSupergateValues::handle);
    }

}
