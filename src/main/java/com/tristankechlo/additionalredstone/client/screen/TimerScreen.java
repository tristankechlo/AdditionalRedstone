package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import com.tristankechlo.additionalredstone.tileentity.TimerTileEntity;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.time.LocalTime;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class TimerScreen extends CustomScreen {

    private static final ITextComponent TITLE = ModBlocks.TIMER_BLOCK.get().getName().withStyle(TextFormatting.BOLD);
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/timer_screen.png");
    private static final ITextComponent POWER_ON = new TranslationTextComponent("screen.additionalredstone.timer.power.on");
    private static final ITextComponent POWER_OFF = new TranslationTextComponent("screen.additionalredstone.timer.power.off");
    private static final ITextComponent INTERVAL = new TranslationTextComponent("screen.additionalredstone.timer.interval");
    private static final ITextComponent DESCRIPTION = new TranslationTextComponent("screen.additionalredstone.timer.description");
    private TextFieldWidget powerUpWidget;
    private TextFieldWidget powerDownWidget;
    private TextFieldWidget intervalWidget;
    private final BlockPos pos;
    private final int initialPowerUpTime;
    private final int initialPowerDownTime;
    private final int initialInterval;
    private boolean powerUpError = false;
    private boolean powerDownError = false;
    private boolean intervalError = false;

    public TimerScreen(int powerUp, int powerDown, int interval, BlockPos pos) {
        super(TITLE, 256, 152);
        this.initialPowerUpTime = powerUp;
        this.initialPowerDownTime = powerDown;
        this.initialInterval = interval;
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        this.powerUpWidget = new TextFieldWidget(this.font, this.leftPos + 176, this.topPos + 24, 70, 20, POWER_ON);
        this.powerDownWidget = new TextFieldWidget(this.font, this.leftPos + 176, this.topPos + 57, 70, 20, POWER_OFF);
        this.intervalWidget = new TextFieldWidget(this.font, this.leftPos + 176, this.topPos + 90, 70, 20, INTERVAL);
        this.powerUpWidget.setMaxLength(10);
        this.powerDownWidget.setMaxLength(10);
        this.intervalWidget.setMaxLength(10);
        this.powerUpWidget.setValue(String.valueOf(this.initialPowerUpTime));
        this.powerDownWidget.setValue(String.valueOf(this.initialPowerDownTime));
        this.intervalWidget.setValue(String.valueOf(this.initialInterval));
        this.children.add(this.powerUpWidget);
        this.children.add(this.powerDownWidget);
        this.children.add(this.intervalWidget);

        this.addSaveButton(this.leftPos + 9, this.topPos + 123, this::save);
        this.addCancelButton(this.leftPos + 131, this.topPos + 123);
    }

    private void save(Button button) {
        int powerUp = getTimeFromEditBox(this.powerUpWidget, (b) -> this.powerUpError = b);
        int powerDown = getTimeFromEditBox(this.powerDownWidget, (b) -> this.powerDownError = b);
        int interval = getValueFromEditBox(this.intervalWidget, (b) -> this.intervalError = b);

        if (this.powerUpError || this.powerDownError || this.intervalError) {
            return;
        }
        PacketHandler.INSTANCE.sendToServer(new SetTimerValues(powerUp, powerDown, interval, pos));
        this.onClose();
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        this.powerUpWidget.render(poseStack, mouseX, mouseY, partialTicks);
        this.powerDownWidget.render(poseStack, mouseX, mouseY, partialTicks);
        this.intervalWidget.render(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        // render title
        this.font.draw(poseStack, this.title, this.leftPos + 9, this.topPos + 6, TEXT_COLOR_SCREEN);

        // render description for the edit boxes
        this.font.draw(poseStack, POWER_ON, this.leftPos + 9, this.topPos + 30, TEXT_COLOR_SCREEN);
        this.font.draw(poseStack, POWER_OFF, this.leftPos + 9, this.topPos + 63, TEXT_COLOR_SCREEN);
        this.font.draw(poseStack, INTERVAL, this.leftPos + 9, this.topPos + 96, TEXT_COLOR_SCREEN);

        // render red cross next to the edit box
        if (this.powerUpError) {
            this.renderErrorIcon(poseStack, this.leftPos + 227, this.topPos + 25);
        }
        if (this.powerDownError) {
            this.renderErrorIcon(poseStack, this.leftPos + 227, this.topPos + 58);
        }
        if (this.intervalError) {
            this.renderErrorIcon(poseStack, this.leftPos + 227, this.topPos + 91);
        }

        // render tooltips over edit boxes when focused
        if (this.intervalWidget.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, TICK_DESCRIPTION, mouseX, mouseY);
        }
        if (this.powerUpWidget.isMouseOver(mouseX, mouseY) || this.powerDownWidget.isMouseOver(mouseX, mouseY)) {
            renderTooltip(poseStack, DESCRIPTION, mouseX, mouseY);
        }
        this.renderCustomButtonTooltips(poseStack, mouseX, mouseY);
    }

    @Override
    public void renderBackground(MatrixStack poseStack) {
        super.renderBackground(poseStack);
        this.renderTexture(poseStack, TEXTURE);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        if (this.powerUpWidget.isFocused()) {
            this.powerUpError = false;
        }
        return super.keyPressed($$0, $$1, $$2);
    }

    private static int getTimeFromEditBox(TextFieldWidget widget, Consumer<Boolean> consumer) {
        int returnTime = 0;
        String text = widget.getValue();
        if (text.equalsIgnoreCase("24:00")) {
            text = "00:00";
        }
        try {
            LocalTime time = LocalTime.parse(text);
            returnTime = (time.getHour() * 1000) + (int) (time.getMinute() * 16.67) - 6000;
            if (returnTime < 0) {
                returnTime = 24000 + returnTime;
            }
            consumer.accept(false);
        } catch (Exception e) {
            try {
                returnTime = Integer.parseInt(text);
            } catch (Exception ex) {
                consumer.accept(true);
            }
        }
        return MathHelper.clamp(returnTime, TimerTileEntity.MIN_TIME, TimerTileEntity.MAX_TIME);
    }

}
