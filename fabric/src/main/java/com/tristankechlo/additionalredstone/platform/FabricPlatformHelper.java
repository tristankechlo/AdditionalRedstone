package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.screen.*;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

@AutoService(IPlatformHelper.class)
public final class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> makeBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block> block) {
        return () -> BlockEntityType.Builder.of(supplier, block.get()).build(null);
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
    @Environment(EnvType.CLIENT)
    public void openSupergateScreen(byte configuration, BlockPos pos) {
        Minecraft.getInstance().setScreen(new SupergateScreen(configuration, pos));
    }

}
