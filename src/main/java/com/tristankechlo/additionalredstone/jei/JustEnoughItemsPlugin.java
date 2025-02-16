package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModRecipes;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JustEnoughItemsPlugin implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation(AdditionalRedstone.MOD_ID, "jei_plugin");
    private IRecipeCategory<CircuitMakerRecipe> recipeCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        //jei features are available
        AdditionalRedstone.LOGGER.info("JEI is available");
        //if jei is available, the circuit maker GUI will have a button to open the JEI GUI instead of the custom tooltip
        AdditionalRedstone.JEI_LOADED = true;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        //custom recipe category for the circuit maker
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(recipeCategory = new CircuitMakerRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        //register recipes for the circuit maker category
        Objects.requireNonNull(recipeCategory, "circuitMakerCategory");
        List<CircuitMakerRecipe> recipes = getRecipes();
        registration.addRecipes(recipes, UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //while in circuit maker GUI, add clickable area to open the JEI GUI for the custom category
        registration.addGuiContainerHandler(CircuitMakerScreen.class, new CircuitMakerGuiHandler());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        //move ingredients from the inventory into crafting GUIs
        registration.addRecipeTransferHandler(CircuitMakerContainer.class, UID, 0, 3, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        //how to craft the thing where the recipe is for (e.g. the circuit maker)
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CIRCUIT_MAKER_BLOCK.get()), UID);
    }

    public static List<CircuitMakerRecipe> getRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        Objects.requireNonNull(minecraft, "minecraft");
        ClientWorld world = minecraft.level;
        Objects.requireNonNull(world, "minecraft world");
        RecipeManager recipeManager = world.getRecipeManager();
        return recipeManager.getAllRecipesFor(ModRecipes.CIRCUIT_MAKER_RECIPE_TYPE.get());
    }

}
