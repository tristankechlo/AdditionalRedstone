package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CircuitMakerRecipeCategory implements IRecipeCategory<CircuitMakerRecipe> {

    private static final ResourceLocation LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/circuit_maker.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public CircuitMakerRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(LOCATION, 0, 213, 107, 43);
        localizedName = Component.translatable("container.additionalredstone.circuit_maker");
        icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get()));
    }

    @Override
    public RecipeType<CircuitMakerRecipe> getRecipeType() {
        return JustEnoughItemsPlugin.RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CircuitMakerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 3, 3).addIngredients(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 3).addIngredients(recipe.getInput2());
        builder.addSlot(RecipeIngredientRole.INPUT, 14, 24).addItemStack(new ItemStack(ModItems.CIRCUIT_BASE_BLOCK_ITEM.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 14).addItemStack(getResultItem(recipe));
    }

    @Override
    public boolean isHandled(CircuitMakerRecipe recipe) {
        return !recipe.isSpecial();
    }

    private static ItemStack getResultItem(CircuitMakerRecipe recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }
        RegistryAccess registryAccess = level.registryAccess();
        return recipe.getResultItem(registryAccess);
    }

}
