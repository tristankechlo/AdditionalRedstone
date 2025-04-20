package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.OnOffButton;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetSupergateValues;
import com.tristankechlo.additionalredstone.tileentity.SupergateTileEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class SupergateScreen extends CustomScreen {

    private static final IFormattableTextComponent TITLE = ModBlocks.SUPERGATE_BLOCK.get().getName();
    private final boolean[] configuration;
    private final BlockPos pos;

    public SupergateScreen(byte configuration, BlockPos pos) {
        super(TITLE, 192, 168);
        this.configuration = SupergateTileEntity.byteToBooleans(configuration);
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            int y = this.topPos + 29 + i * 13;
            OnOffButton button = new OnOffButton(this.leftPos + 139, y, 42, 12, i);
            button.setToggled(this.configuration[i]);
            button.setConsumer(this::setConfig);
            this.addButton(button);
        }

        this.addSaveButton(this.leftPos + 9, this.topPos + 139, 84, 20, this::save);
        this.addCancelButton(this.leftPos + 98, this.topPos + 139, 84, 20);
    }

    private void setConfig(int i, boolean toggled) {
        this.configuration[i] = toggled;
    }

    private void save(Button b) {
        byte data = SupergateTileEntity.booleansToByte(this.configuration);
        PacketHandler.INSTANCE.sendToServer(new SetSupergateValues(data, this.pos));
        this.onClose();
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        // render title
        this.font.draw(poseStack, this.title, this.leftPos + 9, this.topPos + 5, TEXT_COLOR_SCREEN);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        this.font.draw(poseStack, TruthtableScreen.INPUT_A, x + 1, y, 0);
        this.font.draw(poseStack, TruthtableScreen.INPUT_B, x + 44, y, 0);
        this.font.draw(poseStack, TruthtableScreen.INPUT_C, x + 88, y, 0);
        this.font.draw(poseStack, TruthtableScreen.OUTPUT, x + 133, y, 0);

        // render the input states
        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i]; // input states for a, b and c
            y = topPos + 31 + i * 13;
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? OnOffButton.ON : OnOffButton.OFF);
                this.font.draw(poseStack, input[j] ? OnOffButton.ON : OnOffButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0);
            }
        }
        this.renderCustomButtonTooltips(poseStack, mouseX, mouseY);
    }

    @Override
    public void renderBackground(MatrixStack poseStack) {
        super.renderBackground(poseStack);
        // special rendering of this texture to accommodate un-mirrored texture
        // basically skips a single pixel row of the texture
        // noticeable, because buttons can only have an even width of pixels
        this.minecraft.getTextureManager().bind(TruthtableScreen.TEXTURE);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, 181, this.imageHeight);
        blit(poseStack, this.leftPos + 181, this.topPos, 182, 0, this.imageWidth - 182, this.imageHeight);
    }

}
