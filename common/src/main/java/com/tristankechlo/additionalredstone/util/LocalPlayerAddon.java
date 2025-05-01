package com.tristankechlo.additionalredstone.util;

import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import net.minecraft.core.BlockPos;

public interface LocalPlayerAddon {

    void openOscillatorScreen$AdditionalRedstone(int ticksOn, int ticksOff, BlockPos pos);

    void openTimerScreen$AdditionalRedstone(int powerUp, int powerDown, int interval, BlockPos pos);

    void openSequencerScreen$AdditionalRedstone(int interval, BlockPos pos);

    void openTruthtableScreen$AdditionalRedstone(ThreeInputLogicGate block);

    void openSupergateScreen$AdditionalRedstone(byte configuration, BlockPos pos);

}
