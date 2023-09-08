package com.tristankechlo.additionalredstone.platform;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blockentity.*;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.compress.archivers.sevenz.SevenZFileOptions;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface IPlatformHelper {

    public static final IPlatformHelper INSTANCE = Constants.load(IPlatformHelper.class);

    Path getConfigDirectory();

    CreativeModeTab.Builder buildCreativeModeTab();

    BlockEntityType.Builder<OscillatorBlockEntity> buildBETypeOscillator();

    BlockEntityType.Builder<SequencerBlockEntity> buildBlockEntityTypeSequencer();

    BlockEntityType.Builder<TFlipFlopBlockEntity> buildBlockEntityTypeTFlipFlop();

    BlockEntityType.Builder<TimerBlockEntity> buildBlockEntityTypeTimer();

    BlockEntityType.Builder<SuperGateBlockEntity> buildBlockEntityTypeSuperGate();

    Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker();

    void openOscillatorScreen(int ticksOn, int ticksOff, BlockPos pos);

    void openTimerScreen(int powerUp, int powerDown, int interval, BlockPos pos);

    void openSequencerScreen(int interval, BlockPos pos);

    void openTruthtableScreen(ThreeInputLogicGate block);

}
