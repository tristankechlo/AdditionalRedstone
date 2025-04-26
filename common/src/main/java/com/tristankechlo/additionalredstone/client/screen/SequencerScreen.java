package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.IPacketHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SequencerScreen extends CustomScreen {

    private static final Component TITLE = ModBlocks.SEQUENCER_BLOCK.get().getName().withStyle(ChatFormatting.BOLD);
    private static final Component INTERVAL = Component.translatable("screen.additionalredstone.sequencer.interval");
    @SuppressWarnings("removal") // suppress forge deprecation warnings
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/sequencer_screen.png");
    private final BlockPos pos;
    private EditBox intervalWidget;
    private final int initialInterval;
    private boolean intervalError = false;

    public SequencerScreen(int interval, BlockPos pos) {
        super(TITLE, 256, 86);
        this.initialInterval = interval;
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        this.intervalWidget = new EditBox(this.font, this.leftPos + 176, this.topPos + 24, 70, 20, INTERVAL);
        this.intervalWidget.setMaxLength(10);
        this.intervalWidget.setValue(String.valueOf(this.initialInterval));
        this.addRenderableWidget(this.intervalWidget);

        this.addSaveButton(this.leftPos + 9, this.topPos + 57, this::save);
        this.addCancelButton(this.leftPos + 131, this.topPos + 57);
    }

    private void save(Button button) {
        int interval = getValueFromEditBox(this.intervalWidget, (bool) -> this.intervalError = bool);

        if (this.intervalError) {
            return;
        }
        IPacketHelper.INSTANCE.sendPacketSetSequencerValues(interval, pos);
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);

        // render title
        graphics.drawString(this.font, this.title, this.leftPos + 9, this.topPos + 6, TEXT_COLOR_SCREEN, false);

        // render description for the edit boxes
        graphics.drawString(this.font, INTERVAL, this.leftPos + 9, this.topPos + 30, TEXT_COLOR_SCREEN, false);

        // render red cross next to the edit box
        if (this.intervalError) {
            this.renderErrorIcon(graphics, this.leftPos + 227, this.topPos + 25);
        }

        // render tooltips over edit boxes when focused
        if (this.intervalWidget.isMouseOver(mouseX, mouseY)) {
            graphics.renderTooltip(this.font, TICK_DESCRIPTION, mouseX, mouseY);
        }
        this.renderCustomButtonTooltips(graphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int $$1, int $$2, float $$3) {
        super.renderTransparentBackground(graphics);
        this.renderTexture(graphics, TEXTURE);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        if (this.intervalWidget.isFocused()) {
            this.intervalError = false;
        }
        return super.keyPressed($$0, $$1, $$2);
    }

}
