package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blockentity.SuperGateBlockEntity;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.OnOffButton;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.IPacketHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;

public class SupergateScreen extends CustomScreen {

    private static final MutableComponent TITLE = ModBlocks.SUPERGATE_BLOCK.get().getName();
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

        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            int y = this.topPos + 29 + i * 13;
            OnOffButton button = new OnOffButton(this.leftPos + 139, y, 43, 12, i);
            button.setToggled(this.configuration[i]);
            button.setConsumer(this::setConfig);
            this.addRenderableWidget(button);
        }

        Button saveButton = new Button(this.leftPos + 9, this.topPos + 139, 84, 20, TEXT_SAVE, this::save, ONTOOLTIP_SAVE);
        Button cancelButton = new Button(this.leftPos + 99, this.topPos + 139, 84, 20, TEXT_CANCEL, (b) -> this.onClose(), ONTOOLTIP_CANCEL);
        this.addRenderableWidget(saveButton);
        this.addRenderableWidget(cancelButton);
    }

    private void setConfig(int i, boolean toggled) {
        this.configuration[i] = toggled;
    }

    private void save(Button b) {
        byte data = SuperGateBlockEntity.booleansToByte(this.configuration);
        IPacketHandler.INSTANCE.sendPacketSetSupergateValues(data, this.pos);
        AdditionalRedstone.LOGGER.info(Arrays.toString(this.configuration));
        this.onClose();
    }

    @Override
    public void render(PoseStack graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        // render title
        drawString(graphics, this.font, this.title, this.leftPos + 9, this.topPos + 5, TEXT_COLOR_SCREEN);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        drawString(graphics, this.font, TruthtableScreen.INPUT_A, x + 1, y, 0);
        drawString(graphics, this.font, TruthtableScreen.INPUT_B, x + 44, y, 0);
        drawString(graphics, this.font, TruthtableScreen.INPUT_C, x + 88, y, 0);
        drawString(graphics, this.font, TruthtableScreen.OUTPUT, x + 133, y, 0);

        // render the input states
        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i]; // input states for a, b and c
            y = topPos + 31 + i * 13;
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? OnOffButton.ON : OnOffButton.OFF);
                drawString(graphics, this.font, input[j] ? OnOffButton.ON : OnOffButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0);
            }
        }
    }

    @Override
    public void renderBackground(PoseStack graphics) {
        super.renderBackground(graphics);
        this.renderTexture(graphics, TruthtableScreen.TEXTURE);
    }

}
