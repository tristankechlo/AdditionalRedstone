package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdditionalRedstone.MOD_ID);

    public static final IRecipeType<CircuitMakerRecipe> CIRCUIT_MAKER_RECIPE_TYPE = new CircuitMakerRecipe.CircuitMakerRecipeType();
    public static final RegistryObject<CircuitMakerRecipe.Serializer> CIRCUIT_MAKER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("circuit_maker", CircuitMakerRecipe.Serializer::new);

}
