package com.tristankechlo.additionalredstone.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public abstract class CustomScreen extends Screen {

    public static final IFormattableTextComponent TEXT_SAVE = new TranslationTextComponent("screen.additionalredstone.save");
    private static final IFormattableTextComponent TOOLTIP_SAVE = new TranslationTextComponent("screen.additionalredstone.save.tooltip");
    public static final IFormattableTextComponent TEXT_CANCEL = new TranslationTextComponent("screen.additionalredstone.cancel");
    private static final IFormattableTextComponent TOOLTIP_CANCEL = new TranslationTextComponent("screen.additionalredstone.cancel.tooltip");
    public static final int TEXT_COLOR_SCREEN = 4210752; // #404040
    protected static final IFormattableTextComponent TICK_DESCRIPTION = new TranslationTextComponent("screen.additionalredstone.tick.description");
    private static final ResourceLocation ERROR_ICON = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/icons.png");
    private ITextComponent customTitle;
    protected final int imageWidth;
    protected final int imageHeight;
    protected int topPos;
    protected int leftPos;
    protected Button saveButton = null;
    protected Button cancelButton = null;

    protected CustomScreen(ITextComponent title, int imageWidth, int imageHeight) {
        super(title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.customTitle = title;
    }

    @Override
    public ITextComponent getTitle() {
        return this.customTitle;
    }

    public void setTitle(ITextComponent title) {
        this.customTitle = title;
    }

    @Override
    protected void init() {
        super.init();
        // can not be done in constructor, because the width and height are not set yet
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    protected void addSaveButton(int x, int y, Button.IPressable onPress) {
        this.saveButton = new Button(x, y, 116, 20, TEXT_SAVE, onPress);
        this.addButton(saveButton);
    }

    protected void addSaveButton(int x, int y, int width, int height, Button.IPressable onPress) {
        this.saveButton = new Button(x, y, width, height, TEXT_SAVE, onPress);
        this.addButton(saveButton);
    }

    protected void addCancelButton(int x, int y) {
        this.cancelButton = new Button(x, y, 116, 20, TEXT_CANCEL, (b) -> this.onClose());
        this.addButton(cancelButton);
    }

    protected void addCancelButton(int x, int y, int width, int height) {
        this.cancelButton = new Button(x, y, width, height, TEXT_CANCEL, (b) -> this.onClose());
        this.addButton(cancelButton);
    }

    protected void renderCustomButtonTooltips(MatrixStack poseStack, int mouseX, int mouseY) {
        if (this.saveButton != null && this.saveButton.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TOOLTIP_SAVE, mouseX, mouseY);
        }
        if (this.cancelButton != null && this.cancelButton.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TOOLTIP_CANCEL, mouseX, mouseY);
        }
    }

    protected void renderTexture(MatrixStack poseStack, ResourceLocation texture) {
        this.minecraft.getTextureManager().bind(texture);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderErrorIcon(MatrixStack poseStack, int x, int y) {
        this.minecraft.getTextureManager().bind(ERROR_ICON);
        blit(poseStack, x, y, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        // close screen when inventory key is pressed
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches($$0, $$1)) {
            boolean anyFocused = children().stream().anyMatch((child) -> {
                return (child instanceof TextFieldWidget) && (((TextFieldWidget) child).isHovered() || ((TextFieldWidget) child).isFocused());
            });
            if (!anyFocused) {
                this.onClose();
                return true;
            }
        }
        return super.keyPressed($$0, $$1, $$2);
    }

    protected void playButtonClickSound() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected static int getValueFromEditBox(TextFieldWidget widget, Consumer<Boolean> consumer) {
        int value = 0;
        try {
            value = Integer.parseInt(widget.getValue());
            consumer.accept(false);
        } catch (Exception e) {
            consumer.accept(true);
        }
        return value;
    }

    @Override
    public boolean mouseClicked(double x, double y, int key) {
        for (IGuiEventListener child : children()) {
            if (!child.isMouseOver(x, y) && (child instanceof Widget)) {
                ((Widget) child).setFocused(false);
            }
        }
        return super.mouseClicked(x, y, key);
    }

}
