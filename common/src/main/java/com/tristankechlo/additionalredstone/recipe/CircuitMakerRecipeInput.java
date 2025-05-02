package com.tristankechlo.additionalredstone.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record CircuitMakerRecipeInput(ItemStack input_1, ItemStack input_2) implements RecipeInput {

    @Override
    public ItemStack getItem(int i) {
        if (i == 0) {
            return input_1;
        } else if (i == 1) {
            return input_2;
        }
        throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size());
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return input_1.isEmpty() && input_2.isEmpty();
    }
}
