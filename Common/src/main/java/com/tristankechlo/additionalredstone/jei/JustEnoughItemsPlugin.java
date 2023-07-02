package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JustEnoughItemsPlugin implements IModPlugin {

    public static final ResourceLocation UID = new ResourceLocation(Constants.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        //jei features are available
        //if jei is available, the circuit maker GUI will have a button to open the JEI GUI instead of the custom tooltip
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        //custom recipe category for the circuit maker
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        //register recipes for the circuit maker category
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //while in circuit maker GUI, add clickable area to open the JEI GUI for the custom category
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        //move ingredients from the inventory into crafting GUIs
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        //how to craft the thing where the recipe is for (e.g. the circuit maker)
        //registration.addRecipeCatalyst(new ItemStack(ModBlocks.CIRCUIT_MAKER_BLOCK.get()), null);
    }

}
