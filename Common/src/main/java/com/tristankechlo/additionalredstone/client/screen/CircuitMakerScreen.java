package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.container.CircuitMakerContainer;
import com.tristankechlo.additionalredstone.util.Circuits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CircuitMakerScreen extends AbstractContainerScreen<CircuitMakerContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/container/circuit_maker.png");
    private static final ResourceLocation ICONS = new ResourceLocation(Constants.MOD_ID, "textures/other/icons.png");
    private static final ItemStack[] STACKS = new ItemStack[]{new ItemStack(Items.REDSTONE), new ItemStack(Items.QUARTZ), new ItemStack(Items.REDSTONE_TORCH), new ItemStack(Items.STONE_SLAB)};
    private float sliderProgress;
    private boolean clickedOnSroll;
    private int recipeIndexOffset;
    private boolean hasItemsInInputSlot;
    private final int buttonsPerRow = 4;
    private final int buttonSize = 18;
    private boolean renderInputHelp = false;

    public CircuitMakerScreen(CircuitMakerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 192;
        this.imageHeight = 167;
        this.titleLabelY -= 1;
        this.titleLabelX -= 3;
        this.inventoryLabelX -= 2;
        this.inventoryLabelY += 2;
        menu.setInventoryChangeListener(this::onInventoryChange);
    }

    @Override
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // render scrollbar
        int k = (int) (41.0F * this.sliderProgress);
        blit(matrixStack, i + 144, j + 16 + k, 232 + (this.canScroll() ? 0 : 12), 0, 12, 15);

        // render buttons with items
        if (this.hasItemsInInputSlot) {
            int l = this.leftPos + 68;
            int i1 = this.topPos + 15;
            int j1 = this.recipeIndexOffset + 12;
            this.renderButtons(matrixStack, x, y, l, i1, j1);
            this.renderCraftableItems(matrixStack, l, i1, j1);
        }

        // render help for input slots
        if (this.renderInputHelp) {
            this.renderInputHelp(matrixStack, x, y);
        } else {
            int xx = this.leftPos - 25;
            int yy = this.topPos;
            RenderSystem.setShaderTexture(0, ICONS);
            blit(matrixStack, xx, yy, 50, 0, 25, 25);
            if (x >= xx && x < xx + 25 && y >= yy && y < yy + 25) {
                Component helpText = Component.translatable("screen.additionalredstone.circuit_maker.show_recipe");
                this.renderTooltip(matrixStack, helpText, x, y);
            }
        }
    }

    private void renderInputHelp(PoseStack matrixStack, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        blit(matrixStack, this.leftPos - 72, this.topPos, 18, 167, 72, 52);
        final ItemRenderer renderer = this.minecraft.getItemRenderer();
        for (int i = 0; i < 3; i++) {
            int x = this.leftPos - 64 + (i * 20);
            int y = this.topPos + 8;
            renderer.renderGuiItem(matrixStack, STACKS[i], x, y);
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                this.renderTooltip(matrixStack, STACKS[i], mouseX, mouseY);
            }
        }
        for (int i = 0; i < 3; i++) {
            int x = this.leftPos - 64 + (i * 20);
            int y = this.topPos + 28;
            renderer.renderGuiItem(matrixStack, STACKS[3], x, y);
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                this.renderTooltip(matrixStack, STACKS[3], mouseX, mouseY);
            }
        }
        int xx = this.leftPos - 97;
        int yy = this.topPos;
        RenderSystem.setShaderTexture(0, ICONS);
        blit(matrixStack, xx, yy, 25, 0, 25, 25);
        blit(matrixStack, xx + 3, yy + 4, 1, 1, 18, 18);
        if (mouseX >= xx && mouseX < xx + 25 && mouseY >= yy && mouseY < yy + 25) {
            Component helpText = Component.translatable("screen.additionalredstone.close");
            this.renderTooltip(matrixStack, helpText, mouseX, mouseY);
        }
    }

    @Override
    protected void renderTooltip(PoseStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);

        // render tooltips for items in the buttons
        if (this.hasItemsInInputSlot) {
            int i = this.leftPos + 68;
            int j = this.topPos + 15;
            int k = this.recipeIndexOffset + 12;
            Circuits[] list = Circuits.values();
            for (int l = this.recipeIndexOffset; l < k && l < Circuits.SIZE; ++l) {
                int i1 = l - this.recipeIndexOffset;
                int j1 = i + i1 % buttonsPerRow * buttonSize;
                int k1 = j + i1 / buttonsPerRow * buttonSize + 2;
                if (x >= j1 && x < j1 + buttonSize && y >= k1 && y < k1 + buttonSize) {
                    this.renderTooltip(matrixStack, list[l].getItemStack(), x, y);
                }
            }
        }
    }

    private void renderButtons(PoseStack matrixStack, int x, int y, int p1, int p2, int p3) {
        for (int i = this.recipeIndexOffset; i < p3 && i < Circuits.SIZE; ++i) {
            int j = i - this.recipeIndexOffset;
            int k = p1 + j % buttonsPerRow * buttonSize;
            int l = j / buttonsPerRow;
            int i1 = p2 + l * buttonSize + 2;
            int j1 = this.imageHeight;
            if (i == this.menu.getSelectedRecipe() - 1) {
                j1 += buttonSize;
            } else if (x >= k && y >= i1 && x < k + buttonSize && y < i1 + buttonSize) {
                j1 += (2 * buttonSize);
            }
            blit(matrixStack, k, i1 - 1, 0, j1, buttonSize, buttonSize);
        }
    }

    private void renderCraftableItems(PoseStack ps, int left, int top, int recipeIndexOffsetMax) {
        Circuits[] list = Circuits.values();
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < Circuits.SIZE; ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % buttonsPerRow * buttonSize + 1;
            int l = j / buttonsPerRow;
            int i1 = top + l * buttonSize + 1;
            this.minecraft.getItemRenderer().renderAndDecorateItem(ps, list[i].getItemStack(), k, i1);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clickedOnSroll = false;
        if (this.hasItemsInInputSlot) {
            int i = this.leftPos + 68;
            int j = this.topPos + 15;
            int k = this.recipeIndexOffset + 12;

            for (int l = this.recipeIndexOffset; l < k; ++l) {
                int i1 = l - this.recipeIndexOffset;
                double d0 = mouseX - (double) (i + i1 % buttonsPerRow * buttonSize);
                double d1 = mouseY - (double) (j + i1 / buttonsPerRow * buttonSize);
                double buttonSize2 = (double) this.buttonSize;
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < buttonSize2 && d1 < buttonSize2 && this.menu.clickMenuButton(this.minecraft.player, l + 1)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l + 1);
                    return true;
                }
            }
            // scroll bar start
            i = this.leftPos + 152;
            j = this.topPos + 14;
            if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
                this.clickedOnSroll = true;
            }
        }
        if (!this.renderInputHelp) {
            int x = this.leftPos - 25;
            int y = this.topPos;
            if (mouseX >= x && mouseX < x + 25 && mouseY >= y && mouseY < y + 25) {
                this.renderInputHelp = true;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
            }
        } else {
            int x = this.leftPos - 97;
            int y = this.topPos;
            if (mouseX >= x && mouseX < x + 25 && mouseY >= y && mouseY < y + 25) {
                this.renderInputHelp = false;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.clickedOnSroll && this.canScroll()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.sliderProgress = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float) ((double) this.sliderProgress - delta / (double) i);
            this.sliderProgress = Mth.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
        }
        return true;
    }

    private boolean canScroll() {
        return this.hasItemsInInputSlot && Circuits.SIZE > 12;
    }

    private int getHiddenRows() {
        return (Circuits.SIZE + 4 - 1) / 4 - 3;
    }

    private void onInventoryChange() {
        this.hasItemsInInputSlot = this.menu.hasEnoughItemsInSlots();
        if (!this.hasItemsInInputSlot) {
            this.sliderProgress = 0.0F;
            this.recipeIndexOffset = 0;
        }
    }

}
