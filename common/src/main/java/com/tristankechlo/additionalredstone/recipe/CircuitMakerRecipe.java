package com.tristankechlo.additionalredstone.recipe;

import com.google.gson.JsonObject;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CircuitMakerRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient input_1;
    private final Ingredient input_2;
    private final ItemStack result;
    private static ItemStack toastSymbol;

    public CircuitMakerRecipe(ResourceLocation id, Ingredient input_1, Ingredient input_2, ItemStack result) {
        this.id = id;
        this.input_1 = input_1;
        this.input_2 = input_2;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (container.getContainerSize() < 2) {
            return false;
        }
        return (input_1.test(container.getItem(0)) && input_2.test(container.getItem(1)))
                || (input_1.test(container.getItem(1)) && input_2.test(container.getItem(0)));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    public Ingredient getInput1() {
        return input_1;
    }

    public Ingredient getInput2() {
        return input_2;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CIRCUIT_MAKER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        if (toastSymbol == null) {
            toastSymbol = new ItemStack(ModBlocks.CIRCUIT_MAKER_BLOCK.get());
        }
        return toastSymbol;
    }

    public static class Serializer implements RecipeSerializer<CircuitMakerRecipe> {

        @Override
        public CircuitMakerRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input_1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input_1"), false);
            Ingredient input_2 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input_2"), false);
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new CircuitMakerRecipe(id, input_1, input_2, result);
        }

        @Override
        public CircuitMakerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient input_1 = Ingredient.fromNetwork(buffer);
            Ingredient input_2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new CircuitMakerRecipe(id, input_1, input_2, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CircuitMakerRecipe recipe) {
            recipe.input_1.toNetwork(buffer);
            recipe.input_2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }

    }

}
