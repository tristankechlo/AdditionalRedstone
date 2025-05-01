package com.tristankechlo.additionalredstone.platform;

import com.google.auto.service.AutoService;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

@AutoService(IPlatformHelper.class)
public final class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public CreativeModeTab.Builder buildCreativeModeTab() {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> makeBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block> block) {
        return () -> BlockEntityType.Builder.of(supplier, block.get()).build(null);
    }

    @Override
    public Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker() {
        return () -> IMenuTypeExtension.create(CircuitMakerContainer::new);
    }

}
