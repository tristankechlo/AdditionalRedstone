package com.tristankechlo.additionalredstone.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class OnOffButton extends AbstractButton {

    public static final IFormattableTextComponent ON = new TranslationTextComponent("options.on").withStyle(TextFormatting.DARK_GREEN);
    public static final IFormattableTextComponent OFF = new TranslationTextComponent("options.off").withStyle(TextFormatting.DARK_RED);
    private BiConsumer<Integer, Boolean> consumer = null;
    private boolean toggled = false;
    private final int i;

    public OnOffButton(int x, int y, int width, int height, int i) {
        super(x, y, width, height, OFF);
        this.i = i;
    }

    @Override
    public IFormattableTextComponent getMessage() {
        return this.toggled ? ON : OFF;
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
    public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        minecraft.getTextureManager().bind(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int buttonType = this.getYImage(this.isHovered());
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
