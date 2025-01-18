package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.blockentity.*;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.screen.*;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.init.ForgeRecipeSerializer;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.function.Supplier;

@AutoService(IPlatformHelper.class)
public final class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
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
    public BlockEntityType.Builder<LightDetectorBlockEntity> buildBlockEntityTypeLightDetector() {
        return BlockEntityType.Builder.of(LightDetectorBlockEntity::new, ModBlocks.LIGHT_DETECTOR_BLOCK.get());
    }

    @Override
    public Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker() {
        return () -> IForgeMenuType.create(CircuitMakerContainer::new);
    }

    @Override
    public Supplier<RecipeSerializer<CircuitMakerRecipe>> buildRecipeSerializer() {
        return ForgeRecipeSerializer::new;
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openSupergateScreen(byte configuration, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SupergateScreen(configuration, pos));
    }

}
