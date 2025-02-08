package com.tristankechlo.additionalredstone.platform;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    IPlatformHelper INSTANCE = AdditionalRedstone.load(IPlatformHelper.class);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> makeBlockEntityType(BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<Block> block);

    Supplier<MenuType<CircuitMakerContainer>> buildContainerCircuitMaker();

    Supplier<RecipeSerializer<CircuitMakerRecipe>> buildRecipeSerializer();

    void openOscillatorScreen(int ticksOn, int ticksOff, BlockPos pos);

    void openTimerScreen(int powerUp, int powerDown, int interval, BlockPos pos);

    void openSequencerScreen(int interval, BlockPos pos);

    void openTruthtableScreen(ThreeInputLogicGate block);

    void openSupergateScreen(byte configuration, BlockPos pos);

}
