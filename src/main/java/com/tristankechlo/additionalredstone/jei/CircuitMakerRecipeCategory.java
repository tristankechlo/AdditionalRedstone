package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.CircuitMakerBlock;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class CircuitMakerRecipeCategory implements IRecipeCategory<CircuitMakerRecipe> {

    private static final ResourceLocation LOCATION = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/container/circuit_maker.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public CircuitMakerRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(LOCATION, 0, 213, 107, 43);
        localizedName = CircuitMakerBlock.getContainerName().getString();
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get()));
    }

    @Override
    public String getTitle() {
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
    public void setIngredients(CircuitMakerRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Arrays.asList(recipe.getInput1(), recipe.getInput2(), Ingredient.of(ModItems.CIRCUIT_BASE_BLOCK_ITEM.get())));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout builder, CircuitMakerRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = builder.getItemStacks();
        guiItemStacks.init(0, true, 3, 3);
        guiItemStacks.init(1, true, 24, 3);
        guiItemStacks.init(2, true, 14, 24);
        guiItemStacks.init(3, false, 84, 14);
        guiItemStacks.set(ingredients);
    }

    @Override
    public boolean isHandled(CircuitMakerRecipe recipe) {
        return !recipe.isSpecial();
    }

    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return JustEnoughItemsPlugin.UID;
    }

    @SuppressWarnings("removal")
    public Class<? extends CircuitMakerRecipe> getRecipeClass() {
        return CircuitMakerRecipe.class;
    }

}
