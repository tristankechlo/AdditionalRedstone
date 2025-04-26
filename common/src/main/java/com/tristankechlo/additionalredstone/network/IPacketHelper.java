package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.core.BlockPos;

public interface IPacketHelper {

    IPacketHelper INSTANCE = AdditionalRedstone.load(IPacketHelper.class);

    void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos);

    void sendPacketSetSequencerValues(int interval, BlockPos pos);

    void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos);

    void sendPacketSetSupergateValues(byte configuration, BlockPos pos);

}
