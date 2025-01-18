package com.tristankechlo.additionalredstone.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.BiConsumer;

public class OnOffButton extends AbstractButton {

    public static final MutableComponent ON = new TranslatableComponent("options.on").withStyle(ChatFormatting.DARK_GREEN);
    public static final MutableComponent OFF = new TranslatableComponent("options.off").withStyle(ChatFormatting.DARK_RED);
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
    public void updateNarration(NarrationElementOutput output) {
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int buttonType = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        this.blit(poseStack, x, y, 0, 46 + buttonType * 20, width / 2, height / 2); // top left
        this.blit(poseStack, x + width / 2, y, 200 - width / 2, 46 + buttonType * 20, width / 2, height / 2); // top right
        this.blit(poseStack, x, y + (height / 2), 0, 46 + (20 - height / 2) + buttonType * 20, width / 2, height / 2); // bottom left
        this.blit(poseStack, x + width / 2, y + (height / 2), 200 - width / 2, 46 + (20 - height / 2) + buttonType * 20, width / 2, height / 2); // bottom right

        drawCenteredString(poseStack, minecraft.font, this.getMessage(), x + width / 2, y + (height - 8) / 2, 0);
    }

}
