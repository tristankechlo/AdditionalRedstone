package com.tristankechlo.additionalredstone.platform;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;

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

    Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker();

}
