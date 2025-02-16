package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OscillatorScreen extends CustomScreen {

    private static final IFormattableTextComponent TITLE = ModBlocks.OSCILLATOR_BLOCK.get().getName().withStyle(TextFormatting.BOLD);
    private static final IFormattableTextComponent TICKS_ON = new TranslationTextComponent("screen.additionalredstone.oscillator.ticks.on");
    private static final IFormattableTextComponent TICKS_OFF = new TranslationTextComponent("screen.additionalredstone.oscillator.ticks.off");
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/oscillator_screen.png");
    private final BlockPos pos;
    private TextFieldWidget ticksOnWidget;
    private TextFieldWidget ticksOffWidget;
    private final int initialTicksOn; // the initial value of the edit box
    private final int initialTicksOff; // the initial value of the edit box
    private boolean ticksOnError = false; // when true, render the error icon next to the edit box
    private boolean ticksOffError = false; // when true, render the error icon next to the edit box

    public OscillatorScreen(int initialTicksOn, int initialTicksOff, BlockPos pos) {
        super(TITLE, 256, 119);
        this.initialTicksOn = initialTicksOn;
        this.initialTicksOff = initialTicksOff;
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        this.ticksOnWidget = new TextFieldWidget(this.font, this.leftPos + 176, this.topPos + 24, 70, 20, TICKS_ON);
        this.ticksOffWidget = new TextFieldWidget(this.font, this.leftPos + 176, this.topPos + 57, 70, 20, TICKS_OFF);
        this.ticksOnWidget.setMaxLength(10);
        this.ticksOffWidget.setMaxLength(10);
        this.ticksOnWidget.setValue(String.valueOf(this.initialTicksOn));
        this.ticksOffWidget.setValue(String.valueOf(this.initialTicksOff));
        this.children.add(this.ticksOnWidget);
        this.children.add(this.ticksOffWidget);

        this.addSaveButton(this.leftPos + 9, this.topPos + 90, this::save);
        this.addCancelButton(this.leftPos + 131, this.topPos + 90);
    }

    private void save(Button button) {
        int ticks_on = getValueFromEditBox(this.ticksOnWidget, (bool) -> this.ticksOnError = bool);
        int ticks_off = getValueFromEditBox(this.ticksOffWidget, (bool) -> this.ticksOffError = bool);

        if (this.ticksOnError || this.ticksOffError) {
            return;
        }
        PacketHandler.INSTANCE.sendToServer(new SetOscillatorValues(ticks_on, ticks_off, pos));
        this.onClose();
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack); // render texture and transparent background
        super.render(poseStack, mouseX, mouseY, partialTicks); // render buttons and labels

        // render title
        this.font.draw(poseStack, this.title, this.leftPos + 9, this.topPos + 6, TEXT_COLOR_SCREEN);

        // render description for the edit boxes
        this.font.draw(poseStack, TICKS_ON, this.leftPos + 9, this.topPos + 30, TEXT_COLOR_SCREEN);
        this.font.draw(poseStack, TICKS_OFF, this.leftPos + 9, this.topPos + 63, TEXT_COLOR_SCREEN);

        // render red cross next to the edit box
        if (this.ticksOnError) {
            this.renderErrorIcon(poseStack, this.leftPos + 227, this.topPos + 25);
        }
        if (this.ticksOffError) {
            this.renderErrorIcon(poseStack, this.leftPos + 227, this.topPos + 58);
        }

        // render tooltips over edit boxes when focused
        if (this.ticksOnWidget.isMouseOver(mouseX, mouseY) || this.ticksOffWidget.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TICK_DESCRIPTION, mouseX, mouseY);
        }
        this.renderCustomButtonTooltips(poseStack, mouseX, mouseY);
    }

    @Override
    public void renderBackground(MatrixStack poseStack) {
        super.renderBackground(poseStack); // transparent background
        this.renderTexture(poseStack, TEXTURE);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        if (this.ticksOnWidget.isFocused()) {
            this.ticksOnError = false;
        }
        if (this.ticksOffWidget.isFocused()) {
            this.ticksOffError = false;
        }
        return super.keyPressed($$0, $$1, $$2);
    }

}
