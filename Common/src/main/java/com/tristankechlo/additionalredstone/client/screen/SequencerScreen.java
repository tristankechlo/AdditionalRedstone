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

public class SequencerScreen extends Screen {

    private static final Component TITLE = Component.translatable("screen.additionalredstone.sequencer");
    private static final ResourceLocation ERROR = new ResourceLocation(Constants.MOD_ID, "textures/other/icons.png");
    private EditBox intervalWidget;
    private Button saveButton;
    private Button cancelButton;
    private BlockPos pos;
    private int interval;
    private boolean intervalError = false;

    public SequencerScreen(int interval, BlockPos pos) {
        super(TITLE);
        this.interval = interval;
        this.pos = pos;
    }

    @Override
    public void tick() {
        this.intervalWidget.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.intervalWidget = new EditBox(this.font, this.width / 2 + 32, 60, 98, 20, Component.translatable("screen.additionalredstone.sequencer.interval"));
        this.addWidget(this.intervalWidget);
        this.intervalWidget.setMaxLength(10);
        this.setInitialFocus(this.intervalWidget);
        this.intervalWidget.setFocused(true);
        this.intervalWidget.setValue(String.valueOf(this.interval));

        this.saveButton = new Button.Builder(Component.translatable("screen.additionalredstone.save"), (b) -> this.save())
                .pos(this.width / 2 - 110, 150).size(100, 20).build();
        this.cancelButton = new Button.Builder(Component.translatable("screen.additionalredstone.cancel"), (b) -> this.cancel())
                .pos(this.width / 2 + 10, 150).size(100, 20).build();
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    private void save() {
        int interval = 0;
        try {
            interval = Integer.parseInt(this.intervalWidget.getValue());
            this.intervalError = false;
        } catch (Exception e) {
            this.intervalError = true;
        }
        if (this.intervalError) {
            return;
        }
        IPacketHandler.INSTANCE.sendPacketSetSequencerValues(interval, pos);
        this.onClose();
    }

    private void cancel() {
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.intervalWidget.render(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawCenteredString(this.font, Component.translatable("screen.additionalredstone.sequencer.description"),
                this.width / 2, 30, Constants.TEXT_COLOR_SCREEN);

        graphics.drawString(this.font, Component.translatable("screen.additionalredstone.sequencer.interval"),
                this.width / 2 - 130, 65, Constants.TEXT_COLOR_SCREEN);

        if (this.intervalError) {
            graphics.blit(ERROR, this.width / 2 + 140, 61, 1, 1, 18, 18);
        }

    }

}
