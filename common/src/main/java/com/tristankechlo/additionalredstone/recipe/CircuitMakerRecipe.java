package com.tristankechlo.additionalredstone.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record CircuitMakerRecipe(Ingredient input_1, Ingredient input_2, ItemStack result) implements Recipe<CircuitMakerRecipeInput> {

    private static ItemStack toastSymbol;

    @Override
    public boolean matches(CircuitMakerRecipeInput container, Level level) {
        return (input_1.test(container.getItem(0)) && input_2.test(container.getItem(1)))
                || (input_1.test(container.getItem(1)) && input_2.test(container.getItem(0)));
    }

    @Override
    public ItemStack assemble(CircuitMakerRecipeInput container, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
    }

    public Ingredient getInput1() {
        return input_1;
    }

    public Ingredient getInput2() {
        return input_2;
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

        public static final MapCodec<CircuitMakerRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input_1").forGetter(CircuitMakerRecipe::getInput1),
                        Ingredient.CODEC_NONEMPTY.fieldOf("input_2").forGetter(CircuitMakerRecipe::getInput2),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(builder, CircuitMakerRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CircuitMakerRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<CircuitMakerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CircuitMakerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static CircuitMakerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient input_1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient input_2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new CircuitMakerRecipe(input_1, input_2, result);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, CircuitMakerRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input_1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input_2);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }

    }

}
