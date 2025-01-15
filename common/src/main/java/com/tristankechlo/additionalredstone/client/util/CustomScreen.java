package com.tristankechlo.additionalredstone.client.util;

import com.tristankechlo.additionalredstone.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CustomScreen extends Screen {

    public static final MutableComponent TEXT_SAVE = Component.translatable("screen.additionalredstone.save");
    public static final Supplier<Tooltip> TOOLTIP_SAVE = () -> Tooltip.create(Component.translatable("screen.additionalredstone.save.tooltip"));
    public static final MutableComponent TEXT_CANCEL = Component.translatable("screen.additionalredstone.cancel");
    public static final Supplier<Tooltip> TOOLTIP_CANCEL = () -> Tooltip.create(Component.translatable("screen.additionalredstone.cancel.tooltip"));
    public static final MutableComponent TEXT_CLOSE = Component.translatable("screen.additionalredstone.close");
    public static final Supplier<Tooltip> TOOLTIP_CLOSE = () -> Tooltip.create(Component.translatable("screen.additionalredstone.close.tooltip"));
    public static final int TEXT_COLOR_SCREEN = 4210752;
    protected static final MutableComponent TICK_DESCRIPTION = Component.translatable("screen.additionalredstone.tick.description");
    private static final ResourceLocation ERROR_ICON = new ResourceLocation(Constants.MOD_ID, "textures/gui/icons.png");
    private Component customTitle;
    protected final int imageWidth;
    protected final int imageHeight;
    protected int topPos;
    protected int leftPos;

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

    protected void renderTexture(GuiGraphics graphics, ResourceLocation texture) {
        graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void renderErrorIcon(GuiGraphics graphics, int x, int y) {
        graphics.blit(ERROR_ICON, x, y, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        // close screen when inventory key is pressed
        if (this.minecraft != null && this.minecraft.options.keyInventory.matches($$0, $$1)) {
            boolean anyFocused = children().stream().anyMatch((child) -> child.isFocused() && (child instanceof EditBox));
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
            if (!child.isMouseOver(x, y)) {
                child.setFocused(false);
            }
        }
        return super.mouseClicked(x, y, key);
    }
}
