package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.platform.RegistrationProvider;
import com.tristankechlo.additionalredstone.platform.RegistryObject;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class ModRecipes {

    public static final RegistrationProvider<RecipeType<?>> RECIPE_TYPES = RegistrationProvider.get(BuiltInRegistries.RECIPE_TYPE, AdditionalRedstone.MOD_ID);
    public static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(BuiltInRegistries.RECIPE_SERIALIZER, AdditionalRedstone.MOD_ID);

    public static final RegistryObject<RecipeType<CircuitMakerRecipe>> CIRCUIT_MAKER_RECIPE_TYPE = RECIPE_TYPES.register("circuit_maker", () -> new RecipeType<>() {});
    public static final RegistryObject<RecipeSerializer<CircuitMakerRecipe>> CIRCUIT_MAKER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("circuit_maker", () -> new CircuitMakerRecipe.Serializer() {});

    public static void load() {}

}
