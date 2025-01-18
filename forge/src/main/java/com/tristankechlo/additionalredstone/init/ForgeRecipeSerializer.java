package com.tristankechlo.additionalredstone.init;

import com.google.common.reflect.TypeToken;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.GameData;

public class ForgeRecipeSerializer extends CircuitMakerRecipe.Serializer {

    private final TypeToken<RecipeSerializer<?>> token = new TypeToken<>(this.getClass()) {};

    private ResourceLocation registryName;

    @Override
    public final RecipeSerializer<?> setRegistryName(ResourceLocation name) {
        return this.setRegistryName(name.toString());
    }

    ResourceLocation checkRegistryName(String name) {
        return GameData.checkPrefix(name, true);
    }

    public final RecipeSerializer<?> setRegistryName(String name) {
        if (this.getRegistryName() != null) {
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + this.getRegistryName());
        } else {
            this.registryName = this.checkRegistryName(name);
            return this;
        }
    }

    public final RecipeSerializer<?> setRegistryName(String modID, String name) {
        return this.setRegistryName(modID + ":" + name);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<RecipeSerializer<?>> getRegistryType() {
        return (Class<RecipeSerializer<?>>) this.token.getRawType();
    }
}
