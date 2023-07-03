package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class OscillatorScreen extends Screen {

    private static final Component TITLE = Component.translatable("screen.additionalredstone.oscillator");
    private static final ResourceLocation ICONS = new ResourceLocation(Constants.MOD_ID, "textures/gui/icons.png");
    private EditBox ticksOnWidget;
    private EditBox ticksOffWidget;
    private Button saveButton;
    private Button cancelButton;
    private BlockPos pos;
    private int ticksOn;
    private int ticksOff;
    private boolean ticksOnError = false;
    private boolean ticksOffError = false;

    public OscillatorScreen(int ticksOn, int ticksOff, BlockPos pos) {
        super(TITLE);
        this.ticksOn = ticksOn;
        this.ticksOff = ticksOff;
        this.pos = pos;
    }

    @Override
    public void tick() {
        this.ticksOnWidget.tick();
        this.ticksOffWidget.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.ticksOnWidget = new EditBox(this.font, this.width / 2 + 32, 60, 98, 20, Component.translatable("screen.additionalredstone.oscillator.ticks.on"));
        this.ticksOffWidget = new EditBox(this.font, this.width / 2 + 32, 90, 98, 20, Component.translatable("screen.additionalredstone.oscillator.ticks.off"));
        this.addWidget(this.ticksOnWidget);
        this.addWidget(this.ticksOffWidget);
        this.ticksOnWidget.setMaxLength(10);
        this.ticksOffWidget.setMaxLength(10);
        this.setInitialFocus(this.ticksOnWidget);
        this.ticksOnWidget.setFocused(true);
        this.ticksOnWidget.setValue(String.valueOf(this.ticksOn));
        this.ticksOffWidget.setValue(String.valueOf(this.ticksOff));

        this.saveButton = new Button.Builder(Component.translatable("screen.additionalredstone.save"),
                (b) -> this.save()).pos(this.width / 2 - 110, 150).size(100, 20).build();
        this.cancelButton = new Button.Builder(Component.translatable("screen.additionalredstone.cancel"),
                (b) -> this.cancel()).pos(this.width / 2 + 10, 150).size(100, 20).build();
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    private void save() {
        int ticks_on = 0;
        int ticks_off = 0;
        try {
            ticks_on = Integer.parseInt(this.ticksOnWidget.getValue());
            this.ticksOnError = false;
        } catch (Exception e) {
            this.ticksOnError = true;
        }
        try {
            ticks_off = Integer.parseInt(this.ticksOffWidget.getValue());
            this.ticksOffError = false;
        } catch (Exception e) {
            this.ticksOffError = true;
        }
        if (this.ticksOnError || this.ticksOffError) {
            return;
        }
        IPacketHandler.INSTANCE.sendPacketSetOscillatorValues(ticks_on, ticks_off, this.pos);
        this.onClose();
    }

    private void cancel() {
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.ticksOnWidget.render(graphics, mouseX, mouseY, partialTicks);
        this.ticksOffWidget.render(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawCenteredString(this.font, Component.translatable("screen.additionalredstone.oscillator.description"),
                this.width / 2, 30, Constants.TEXT_COLOR_SCREEN);

        graphics.drawString(this.font, Component.translatable("screen.additionalredstone.oscillator.ticks.on"),
                this.width / 2 - 130, 65, Constants.TEXT_COLOR_SCREEN);

        graphics.drawString(this.font, Component.translatable("screen.additionalredstone.oscillator.ticks.off"),
                this.width / 2 - 130, 95, Constants.TEXT_COLOR_SCREEN);

        if (this.ticksOnError) {
            graphics.blit(ICONS, this.width / 2 + 140, 61, 0, 0, 18, 18, 18, 18);
        }
        if (this.ticksOffError) {
            graphics.blit(ICONS, this.width / 2 + 140, 91, 0, 0, 18, 18, 18, 18);
        }
    }

}
