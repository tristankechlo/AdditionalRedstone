package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.recipe.CircuitMakerRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class CircuitMakerScreen extends AbstractContainerScreen<CircuitMakerContainer> {

    private static final ResourceLocation BG_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/circuit_maker.png");
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 18;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 64;
    private static final int RECIPES_Y = 16;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public CircuitMakerScreen(CircuitMakerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 192;
        this.imageHeight = 167;
        this.titleLabelY -= 1;
        this.titleLabelX -= 3;
        this.inventoryLabelX -= 2;
        this.inventoryLabelY += 2;
        menu.setInventoryChangeListener(this::containerChanged);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        this.renderBackground(graphics);

        //render background image
        graphics.blit(BG_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        //render scrollbar
        int offset = (int) (39.0F * this.scrollOffs);
        graphics.blit(BG_LOCATION, this.leftPos + 140, this.topPos + 16 + offset, 214 + (this.isScrollBarActive() ? 0 : SCROLLER_WIDTH), 0, SCROLLER_WIDTH, SCROLLER_HEIGHT);

        //render recipes and buttons
        int $$7 = this.leftPos + RECIPES_X;
        int $$8 = this.topPos + RECIPES_Y;
        int $$9 = this.startIndex + SCROLLER_WIDTH;
        this.renderButtons(graphics, x, y, $$7, $$8, $$9);
        this.renderRecipes(graphics, $$7, $$8, $$9);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        super.renderTooltip(graphics, x, y);
        if (this.displayRecipes) {
            int $$3 = this.leftPos + RECIPES_X;
            int $$4 = this.topPos + RECIPES_Y;
            int $$5 = this.startIndex + SCROLLER_WIDTH;
            List<CircuitMakerRecipe> recipes = this.menu.getRecipes();

            for (int $$7 = this.startIndex; $$7 < $$5 && $$7 < (this.menu).getNumRecipes(); ++$$7) {
                int $$8 = $$7 - this.startIndex;
                int $$9 = $$3 + $$8 % 4 * RECIPES_IMAGE_SIZE_WIDTH;
                int $$10 = $$4 + $$8 / 4 * RECIPES_IMAGE_SIZE_HEIGHT + 2;
                if (x >= $$9 && x < $$9 + RECIPES_IMAGE_SIZE_WIDTH && y >= $$10 && y < $$10 + RECIPES_IMAGE_SIZE_HEIGHT) {
                    graphics.renderTooltip(this.font, recipes.get($$7).getResultItem(this.minecraft.level.registryAccess()), x, y);
                }
            }
        }
    }

    private void renderButtons(GuiGraphics graphics, int x, int y, int $$3, int $$4, int $$5) {
        for (int index = this.startIndex; index < $$5 && index < (this.menu).getNumRecipes(); ++index) {
            int $$7 = index - this.startIndex;
            int $$8 = $$3 + $$7 % 4 * RECIPES_IMAGE_SIZE_WIDTH;
            int $$9 = $$7 / 4;
            int $$10 = $$4 + $$9 * RECIPES_IMAGE_SIZE_HEIGHT + 1;
            int yPosButtonTexture = 0;
            if (index == (this.menu).getSelectedRecipe()) {
                yPosButtonTexture += 18;
            } else if (x >= $$8 && y >= $$10 && x < $$8 + RECIPES_IMAGE_SIZE_WIDTH && y < $$10 + RECIPES_IMAGE_SIZE_HEIGHT) {
                yPosButtonTexture += 36;
            }

            graphics.blit(BG_LOCATION, $$8, $$10 - 1, 238, yPosButtonTexture, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
        }
    }

    private void renderRecipes(GuiGraphics graphics, int $$1, int $$2, int $$3) {
        List<CircuitMakerRecipe> recipes = (this.menu).getRecipes();
        for (int index = this.startIndex; index < $$3 && index < (this.menu).getNumRecipes(); ++index) {
            int $$6 = index - this.startIndex;
            int $$7 = $$1 + $$6 % 4 * RECIPES_IMAGE_SIZE_WIDTH + 1;
            int $$8 = $$6 / 4;
            int $$9 = $$2 + $$8 * RECIPES_IMAGE_SIZE_HEIGHT + 1;
            graphics.renderItem(recipes.get(index).getResultItem(this.minecraft.level.registryAccess()), $$7, $$9);
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int $$3 = this.leftPos + RECIPES_X;
            int $$4 = this.topPos + RECIPES_Y;
            int $$5 = this.startIndex + SCROLLER_WIDTH;

            for (int $$6 = this.startIndex; $$6 < $$5; ++$$6) {
                int $$7 = $$6 - this.startIndex;
                double $$8 = mouseX - (double) ($$3 + $$7 % 4 * RECIPES_IMAGE_SIZE_WIDTH);
                double $$9 = mouseY - (double) ($$4 + $$7 / 4 * RECIPES_IMAGE_SIZE_HEIGHT);
                if ($$8 >= 0.0 && $$9 >= 0.0 && $$8 < RECIPES_IMAGE_SIZE_WIDTH && $$9 < RECIPES_IMAGE_SIZE_HEIGHT && (this.menu).clickMenuButton(this.minecraft.player, $$6)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, $$6);
                    return true;
                }
            }

            $$3 = this.leftPos + 140;
            $$4 = this.topPos + 16;
            if (mouseX >= (double) $$3 && mouseX < (double) ($$3 + SCROLLER_WIDTH) && mouseY >= (double) $$4 && mouseY < (double) ($$4 + SCROLLER_FULL_HEIGHT)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int key, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int startScrollBarY = this.topPos + RECIPES_Y;
            int endScrollBarY = startScrollBarY + SCROLLER_FULL_HEIGHT;
            this.scrollOffs = ((float) mouseY - (float) startScrollBarY - 7.5F) / ((float) (endScrollBarY - startScrollBarY) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, key, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive()) {
            int offscreenRows = this.getOffscreenRows();
            float $$4 = (float) delta / (float) offscreenRows;
            this.scrollOffs = Mth.clamp(this.scrollOffs - $$4, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) offscreenRows) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && (this.menu).getNumRecipes() > SCROLLER_WIDTH;
    }

    protected int getOffscreenRows() {
        return ((this.menu).getNumRecipes() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = (this.menu).hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }

}
