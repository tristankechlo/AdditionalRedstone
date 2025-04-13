package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rect2i;

import java.util.Collection;
import java.util.List;

public class CircuitMakerGuiHandler implements IGuiContainerHandler<CircuitMakerScreen> {

    private static final Rect2i RELATIVE_AREA = new Rect2i(189, 0, 18, 22);
    private static final List<RecipeType<?>> TYPES = List.of(JustEnoughItemsPlugin.RECIPE_TYPE);
    private static final List<IGuiClickableArea> CLICKABLE_AREAS = List.of(new IGuiClickableArea() {
        @Override
        public Rect2i getArea() {
            return RELATIVE_AREA;
        }

        @Override
        public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
            recipesGui.showTypes(TYPES);
        }
    });

    @Override
    public List<Rect2i> getGuiExtraAreas(CircuitMakerScreen screen) {
        return List.of(new Rect2i(screen.getStartX(), screen.getStartY(), 18, 22));
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(CircuitMakerScreen containerScreen, double guiMouseX, double guiMouseY) {
        return CLICKABLE_AREAS;
    }

}
