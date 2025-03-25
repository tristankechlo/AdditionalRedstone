package com.tristankechlo.additionalredstone.recipe;

import com.google.gson.JsonObject;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CircuitMakerRecipe implements IRecipe<IInventory> {

    public static final ResourceLocation TYPE_ID = new ResourceLocation(AdditionalRedstone.MOD_ID, "circuit_maker");
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
    public boolean matches(IInventory container, World level) {
        if (container.getContainerSize() < 2) {
            return false;
        }
        return (input_1.test(container.getItem(0)) && input_2.test(container.getItem(1)))
                || (input_1.test(container.getItem(1)) && input_2.test(container.getItem(0)));
    }

    @Override
    public ItemStack assemble(IInventory container) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.CIRCUIT_MAKER_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE;
    }

    @Override
    public ItemStack getToastSymbol() {
        if (toastSymbol == null) {
            toastSymbol = new ItemStack(ModBlocks.CIRCUIT_MAKER_BLOCK.get());
        }
        return toastSymbol;
    }

    public static class CircuitMakerRecipeType implements IRecipeType<CircuitMakerRecipe> {
        @Override
        public String toString() {
            return TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CircuitMakerRecipe> {

        @Override
        public CircuitMakerRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            Ingredient input_1 = Ingredient.fromNetwork(buffer);
            Ingredient input_2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new CircuitMakerRecipe(id, input_1, input_2, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CircuitMakerRecipe recipe) {
            recipe.input_1.toNetwork(buffer);
            recipe.input_2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }

        @Override
        public CircuitMakerRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input_1 = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input_1"));
            Ingredient input_2 = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input_2"));
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new CircuitMakerRecipe(id, input_1, input_2, result);
        }
    }

}
