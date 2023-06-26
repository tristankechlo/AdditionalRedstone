package com.tristankechlo.additionalredstone.network;

import com.tristankechlo.additionalredstone.Constants;
import net.minecraft.core.BlockPos;

public interface IPacketHandler {

    IPacketHandler INSTANCE = Constants.load(IPacketHandler.class);

    void sendPacketSetOscillatorValues(int ticksOn, int ticksOff, BlockPos pos);

    void sendPacketSetSequencerValues(int interval, BlockPos pos);

    void sendPacketSetTimerValues(int powerUpTime, int powerDownTime, int interval, BlockPos pos);

}
