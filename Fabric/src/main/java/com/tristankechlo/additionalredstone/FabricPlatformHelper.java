package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.blockentity.*;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.screen.*;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.nio.file.Path;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public CreativeModeTab.Builder buildCreativeModeTab() {
        return FabricItemGroup.builder();
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
    public BlockEntityType.Builder<SuperGateBlockEntity> buildBlockEntityTypeSuperGate() {
        return BlockEntityType.Builder.of(SuperGateBlockEntity::new, ModBlocks.SUPERGATE_BLOCK.get());
    }

    @Override
    public Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker() {
        return () -> new MenuType<>(CircuitMakerContainer::new, FeatureFlags.VANILLA_SET);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void openOscillatorScreen(int ticksOn, int ticksOff, BlockPos pos) {
        Minecraft.getInstance().setScreen(new OscillatorScreen(ticksOn, ticksOff, pos));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void openTimerScreen(int powerUp, int powerDown, int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new TimerScreen(powerUp, powerDown, interval, pos));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void openSequencerScreen(int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SequencerScreen(interval, pos));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void openTruthtableScreen(ThreeInputLogicGate block) {
        Minecraft.getInstance().setScreen(new TruthtableScreen(block));
    }

    @Override
    public void openSupergateScreen(byte configuration, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SupergateScreen(configuration, pos));
    }

}
