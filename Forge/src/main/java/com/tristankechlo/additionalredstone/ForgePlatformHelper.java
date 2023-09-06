package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.blockentity.OscillatorBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.SequencerBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TFlipFlopBlockEntity;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.screen.OscillatorScreen;
import com.tristankechlo.additionalredstone.client.screen.SequencerScreen;
import com.tristankechlo.additionalredstone.client.screen.TimerScreen;
import com.tristankechlo.additionalredstone.client.screen.TruthtableScreen;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.platform.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openOscillatorScreen(int ticksOn, int ticksOff, BlockPos pos) {
        Minecraft.getInstance().setScreen(new OscillatorScreen(ticksOn, ticksOff, pos));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openTimerScreen(int powerUp, int powerDown, int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new TimerScreen(powerUp, powerDown, interval, pos));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openSequencerScreen(int interval, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SequencerScreen(interval, pos));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openTruthtableScreen(ThreeInputLogicGate block) {
        Minecraft.getInstance().setScreen(new TruthtableScreen(block));
    }

}
