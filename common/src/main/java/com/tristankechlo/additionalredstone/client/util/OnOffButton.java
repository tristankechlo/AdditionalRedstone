package com.tristankechlo.additionalredstone.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.BiConsumer;

public class OnOffButton extends AbstractButton {

    public static final MutableComponent ON = Component.translatable("options.on").withStyle(ChatFormatting.DARK_GREEN);
    public static final MutableComponent OFF = Component.translatable("options.off").withStyle(ChatFormatting.DARK_RED);
    private BiConsumer<Integer, Boolean> consumer = null;
    private boolean toggled = false;
    private final int i;

    public OnOffButton(int x, int y, int width, int height, int i) {
        super(x, y, width, height, OFF);
        this.i = i;
    }

    @Override
    public MutableComponent getMessage() {
        return this.toggled ? ON : OFF;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public void onPress() {
        this.toggled = !this.toggled;
        setToggled(this.toggled);
    }

    private void updateMessage() {
        setMessage(this.toggled ? ON : OFF);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        updateMessage();
        if (this.consumer != null) {
            this.consumer.accept(this.i, this.toggled);
        }
    }

    public void setConsumer(BiConsumer<Integer, Boolean> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        int buttonType = this.getTextureY();

        graphics.blit(WIDGETS_LOCATION, getX(), getY(), 0, buttonType, width / 2, height / 2); // top left
        graphics.blit(WIDGETS_LOCATION, getX() + width / 2, getY(), 200 - width / 2, buttonType, width / 2, height / 2); // top right
        graphics.blit(WIDGETS_LOCATION, getX(), getY() + (height / 2), 0, (20 - height / 2) + buttonType, width / 2, height / 2); // bottom left
        graphics.blit(WIDGETS_LOCATION, getX() + width / 2, getY() + (height / 2), 200 - width / 2, (20 - height / 2) + buttonType, width / 2, height / 2); // bottom right

        graphics.drawCenteredString(minecraft.font, this.getMessage(), getX() + width / 2, getY() + (height - 8) / 2, 0);
    }

}
