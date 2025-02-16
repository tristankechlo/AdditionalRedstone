package com.tristankechlo.additionalredstone.jei;

import com.tristankechlo.additionalredstone.client.screen.CircuitMakerScreen;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CircuitMakerGuiHandler implements IGuiContainerHandler<CircuitMakerScreen> {

    private static final Rectangle2d RELATIVE_AREA = new Rectangle2d(189, 0, 18, 22);
    private static final List<ResourceLocation> CATEGORIES = Collections.singletonList(JustEnoughItemsPlugin.UID);
    private static final List<IGuiClickableArea> CLICKABLE_AREAS = Collections.singletonList(new IGuiClickableArea() {
        @Override
        public Rectangle2d getArea() {
            return RELATIVE_AREA;
        }

        @Override
        public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
            recipesGui.showCategories(CATEGORIES);
        }
    });

    @Override
    public List<Rectangle2d> getGuiExtraAreas(CircuitMakerScreen screen) {
        return Collections.singletonList(new Rectangle2d(screen.getStartX(), screen.getStartY(), 18, 22));
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(CircuitMakerScreen containerScreen, double guiMouseX, double guiMouseY) {
        return CLICKABLE_AREAS;
    }

}
