package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.ToggleButton;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class SupergateScreen extends CustomScreen {

    private static final Component TITLE = ModBlocks.SUPERGATE_BLOCK.get().getName();
    private final boolean[] configuration;
    private final BlockPos pos;

    public SupergateScreen(byte configuration, BlockPos pos) {
        super(TITLE, 192, 168);
        this.configuration = SuperGateBlockEntity.byteToBooleans(configuration);
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < Constants.INPUT_STATES.length; i++) {
            int y = this.topPos + 29 + i * 13;
            ToggleButton button = new ToggleButton(this.leftPos + 139, y, 43, 12, i);
            button.setToggled(this.configuration[i]);
            button.setConsumer(this::setConfig);
            this.addRenderableWidget(button);
        }

        Button saveButton = new Button.Builder(CustomScreen.TEXT_SAVE, this::save)
                .bounds(this.leftPos + 9, this.topPos + 139, 84, 20)
                .tooltip(CustomScreen.TOOLTIP_SAVE.get()).build();
        this.addRenderableWidget(saveButton);

        Button cancelButton = new Button.Builder(CustomScreen.TEXT_CANCEL, (b) -> this.onClose())
                .bounds(this.leftPos + 99, this.topPos + 139, 84, 20)
                .tooltip(CustomScreen.TOOLTIP_CANCEL.get()).build();
        this.addRenderableWidget(cancelButton);
    }

    private void setConfig(int i, boolean toggled) {
        this.configuration[i] = toggled;
    }

    private void save(Button b) {
        byte data = SuperGateBlockEntity.booleansToByte(this.configuration);
        IPacketHandler.INSTANCE.sendPacketSetSupergateValues(data, this.pos);
        Constants.LOGGER.info(Arrays.toString(this.configuration));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        // render title
        graphics.drawString(this.font, this.title, this.leftPos + 9, this.topPos + 5, TEXT_COLOR_SCREEN, false);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        graphics.drawString(this.font, TruthtableScreen.INPUT_A, x + 1, y, 0, false);
        graphics.drawString(this.font, TruthtableScreen.INPUT_B, x + 44, y, 0, false);
        graphics.drawString(this.font, TruthtableScreen.INPUT_C, x + 88, y, 0, false);
        graphics.drawString(this.font, TruthtableScreen.OUTPUT, x + 133, y, 0, false);

        // render the input states
        for (int i = 0; i < Constants.INPUT_STATES.length; i++) {
            boolean[] input = Constants.INPUT_STATES[i]; // input states for a, b and c
            y = topPos + 31 + i * 13;
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? ToggleButton.ON : ToggleButton.OFF);
                graphics.drawString(this.font, input[j] ? ToggleButton.ON : ToggleButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0, false);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);
        this.renderTexture(graphics, TruthtableScreen.TEXTURE);
    }
}
