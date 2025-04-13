package com.tristankechlo.additionalredstone.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.mixin.client.AbstractWidgetMixin;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;

public abstract class CustomScreen extends Screen {

    public static final MutableComponent TEXT_SAVE = Component.translatable("screen.additionalredstone.save");
    private static final MutableComponent TOOLTIP_SAVE = Component.translatable("screen.additionalredstone.save.tooltip");
    public static final MutableComponent TEXT_CANCEL = Component.translatable("screen.additionalredstone.cancel");
    private static final MutableComponent TOOLTIP_CANCEL = Component.translatable("screen.additionalredstone.cancel.tooltip");
    public static final int TEXT_COLOR_SCREEN = 4210752; // #404040
    protected static final MutableComponent TICK_DESCRIPTION = Component.translatable("screen.additionalredstone.tick.description");
    private static final ResourceLocation ERROR_ICON = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/icons.png");
    private Component customTitle;
    protected final int imageWidth;
    protected final int imageHeight;
    protected int topPos;
    protected int leftPos;
    protected Button saveButton = null;
    protected Button cancelButton = null;

    protected CustomScreen(Component title, int imageWidth, int imageHeight) {
        super(title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.customTitle = title;
    }

    @Override
    public Component getTitle() {
        return this.customTitle;
    }

    public void setTitle(Component title) {
        this.customTitle = title;
    }

    @Override
    protected void init() {
        super.init();
        // can not be done in constructor, because the width and height are not set yet
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    protected void addSaveButton(int x, int y, Button.OnPress onPress) {
        this.saveButton = new Button.Builder(TEXT_SAVE, onPress).bounds(x, y, 116, 20).build();
        this.addRenderableWidget(saveButton);
    }

    protected void addSaveButton(int x, int y, int width, int height, Button.OnPress onPress) {
        this.saveButton = new Button.Builder(TEXT_SAVE, onPress).bounds(x, y, width, height).build();
        this.addRenderableWidget(saveButton);
    }

    protected void addCancelButton(int x, int y) {
        this.cancelButton = new Button.Builder(TEXT_CANCEL, (b) -> this.onClose()).bounds(x, y, 116, 20).build();
        this.addRenderableWidget(cancelButton);
    }

    protected void addCancelButton(int x, int y, int width, int height) {
        this.cancelButton = new Button.Builder(TEXT_CANCEL, (b) -> this.onClose()).bounds(x, y, width, height).build();
        this.addRenderableWidget(cancelButton);
    }

    protected void renderCustomButtonTooltips(PoseStack poseStack, int mouseX, int mouseY) {
        if (this.saveButton != null && this.saveButton.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TOOLTIP_SAVE, mouseX, mouseY);
        }
        if (this.cancelButton != null && this.cancelButton.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TOOLTIP_CANCEL, mouseX, mouseY);
        }
    }

    protected void renderTexture(PoseStack poseStack, ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderErrorIcon(PoseStack poseStack, int x, int y) {
        RenderSystem.setShaderTexture(0, ERROR_ICON);
        blit(poseStack, x, y, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        // close screen when inventory key is pressed
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches($$0, $$1)) {
            boolean anyFocused = children().stream().anyMatch((child) -> {
                return (child instanceof EditBox) && ((EditBox) child).isHoveredOrFocused();
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
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected static int getValueFromEditBox(EditBox widget, Consumer<Boolean> consumer) {
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
        for (GuiEventListener child : children()) {
            if (!child.isMouseOver(x, y) && (child instanceof AbstractWidget widget)) {
                ((AbstractWidgetMixin) widget).setFocused(false);
            }
        }
        return super.mouseClicked(x, y, key);
    }

}
