package com.tristankechlo.additionalredstone.init;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = AdditionalRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdditionalRedstone.MOD_ID);

    public static Supplier<IRecipeType<CircuitMakerRecipe>> CIRCUIT_MAKER_RECIPE_TYPE = () -> null;
    public static final RegistryObject<IRecipeSerializer<CircuitMakerRecipe>> CIRCUIT_MAKER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("circuit_maker", CircuitMakerRecipe.Serializer::new);


    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CIRCUIT_MAKER_RECIPE_TYPE = CircuitMakerRecipeType::new;
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(AdditionalRedstone.MOD_ID, "circuit_maker"), CIRCUIT_MAKER_RECIPE_TYPE.get());
    }

    private static class CircuitMakerRecipeType implements IRecipeType<CircuitMakerRecipe> {
        @Override
        public String toString() {
            return "circuit_maker";
        }
    }

}
