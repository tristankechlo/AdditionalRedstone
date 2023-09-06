package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blockentity.TimerBlockEntity;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.time.LocalTime;
import java.util.function.Consumer;

public class TimerScreen extends CustomScreen {

    private static final Component TITLE = ModBlocks.TIMER_BLOCK.get().getName().withStyle(ChatFormatting.BOLD);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/timer_screen.png");
    private static final Component POWER_ON = Component.translatable("screen.additionalredstone.timer.power.on");
    private static final Component POWER_OFF = Component.translatable("screen.additionalredstone.timer.power.off");
    private static final Component INTERVAL = Component.translatable("screen.additionalredstone.timer.interval");
    private static final Component DESCRIPTION = Component.translatable("screen.additionalredstone.timer.description");
    private EditBox powerUpWidget;
    private EditBox powerDownWidget;
    private EditBox intervalWidget;
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
        this.powerUpWidget = new EditBox(this.font, this.leftPos + 176, this.topPos + 24, 70, 20, POWER_ON);
        this.powerDownWidget = new EditBox(this.font, this.leftPos + 176, this.topPos + 57, 70, 20, POWER_OFF);
        this.intervalWidget = new EditBox(this.font, this.leftPos + 176, this.topPos + 90, 70, 20, INTERVAL);
        this.powerUpWidget.setMaxLength(10);
        this.powerDownWidget.setMaxLength(10);
        this.intervalWidget.setMaxLength(10);
        this.powerUpWidget.setValue(String.valueOf(this.initialPowerUpTime));
        this.powerDownWidget.setValue(String.valueOf(this.initialPowerDownTime));
        this.intervalWidget.setValue(String.valueOf(this.initialInterval));
        this.addRenderableWidget(this.powerUpWidget);
        this.addRenderableWidget(this.powerDownWidget);
        this.addRenderableWidget(this.intervalWidget);

        Button saveButton = new Button.Builder(CustomScreen.TEXT_SAVE, this::save)
                .pos(this.leftPos + 9, this.topPos + 123).size(116, 20)
                .tooltip(CustomScreen.TOOLTIP_SAVE.get()).build();
        Button cancelButton = new Button.Builder(CustomScreen.TEXT_CANCEL, (b) -> this.onClose())
                .pos(this.leftPos + 131, this.topPos + 123).size(116, 20)
                .tooltip(CustomScreen.TOOLTIP_CANCEL.get()).build();
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    private void save(Button button) {
        int powerUp = getTimeFromEditBox(this.powerUpWidget, (b) -> this.powerUpError = b);
        int powerDown = getTimeFromEditBox(this.powerDownWidget, (b) -> this.powerDownError = b);
        int interval = getValueFromEditBox(this.intervalWidget, (b) -> this.intervalError = b);

        if (this.powerUpError || this.powerDownError || this.intervalError) {
            return;
        }
        IPacketHandler.INSTANCE.sendPacketSetTimerValues(powerUp, powerDown, interval, pos);
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        // render title
        graphics.drawString(this.font, this.title, this.leftPos + 9, this.topPos + 6, 4210752, false);

        // render description for the edit boxes
        graphics.drawString(this.font, POWER_ON, this.leftPos + 9, this.topPos + 30, 4210752, false);
        graphics.drawString(this.font, POWER_OFF, this.leftPos + 9, this.topPos + 63, 4210752, false);
        graphics.drawString(this.font, INTERVAL, this.leftPos + 9, this.topPos + 96, 4210752, false);

        // render red cross next to the edit box
        if (this.powerUpError) {
            this.renderErrorIcon(graphics, this.leftPos + 227, this.topPos + 25);
        }
        if (this.powerDownError) {
            this.renderErrorIcon(graphics, this.leftPos + 227, this.topPos + 58);
        }
        if (this.intervalError) {
            this.renderErrorIcon(graphics, this.leftPos + 227, this.topPos + 91);
        }

        // render tooltips over edit boxes when focused
        if (this.intervalWidget.isMouseOver(mouseX, mouseY)) {
            graphics.renderTooltip(this.font, CustomScreen.TICK_DESCRIPTION, mouseX, mouseY);
        }
        if (this.powerUpWidget.isMouseOver(mouseX, mouseY) || this.powerDownWidget.isMouseOver(mouseX, mouseY)) {
            graphics.renderTooltip(this.font, DESCRIPTION, mouseX, mouseY);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);
        this.renderTexture(graphics, TEXTURE);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        if (this.powerUpWidget.isFocused()) {
            this.powerUpError = false;
        }
        return super.keyPressed($$0, $$1, $$2);
    }

    private static int getTimeFromEditBox(EditBox widget, Consumer<Boolean> consumer) {
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
        return Mth.clamp(returnTime, TimerBlockEntity.minTime, TimerBlockEntity.maxTime);
    }

}
