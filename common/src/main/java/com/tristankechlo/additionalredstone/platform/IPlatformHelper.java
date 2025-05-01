package com.tristankechlo.additionalredstone.platform;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    IPlatformHelper INSTANCE = AdditionalRedstone.load(IPlatformHelper.class);

    CreativeModeTab.Builder buildCreativeModeTab();

    <T extends BlockEntity> Supplier<BlockEntityType<T>> makeBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block> block);

    Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker();

}
