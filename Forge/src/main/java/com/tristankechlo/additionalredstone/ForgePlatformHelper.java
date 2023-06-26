package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public CreativeModeTab.Builder buildCreativeModeTab() {
        return CreativeModeTab.builder();
    }

    @Override
    public BlockEntityType.Builder<OscillatorBlockEntity> buildBETypeOscillator() {
        return BlockEntityType.Builder.of(OscillatorBlockEntity::new, ModBlocks.OSCILLATOR_BLOCK.get());
    }

    @Override
    public BlockEntityType.Builder<SequencerBlockEntity> buildBlockEntityTypeSequencer() {
        return BlockEntityType.Builder.of(SequencerBlockEntity::new, ModBlocks.SEQUENCER_BLOCK.get());
    }

    @Override
    public BlockEntityType.Builder<TFlipFlopBlockEntity> buildBlockEntityTypeTFlipFlop() {
        return BlockEntityType.Builder.of(TFlipFlopBlockEntity::new, ModBlocks.T_FLIP_FLOP_BLOCK.get());
    }

    @Override
    public BlockEntityType.Builder<TimerBlockEntity> buildBlockEntityTypeTimer() {
        return BlockEntityType.Builder.of(TimerBlockEntity::new, ModBlocks.TIMER_BLOCK.get());
    }

    @Override
    public Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker() {
        return () -> IForgeMenuType.create(CircuitMakerContainer::new);
    }

}
