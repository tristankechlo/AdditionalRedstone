package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.OnOffButton;
import com.tristankechlo.additionalredstone.client.util.TruthTableHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TruthtableScreen extends CustomScreen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/truthtable.png");
    private static final String START = "screen.additionalredstone.truthtable.";
    public static final IFormattableTextComponent INPUT_A = new TranslationTextComponent(START + "input_a").withStyle(TextFormatting.BLACK);
    public static final IFormattableTextComponent INPUT_B = new TranslationTextComponent(START + "input_b").withStyle(TextFormatting.BLACK);
    public static final IFormattableTextComponent INPUT_C = new TranslationTextComponent(START + "input_c").withStyle(TextFormatting.BLACK);
    public static final IFormattableTextComponent OUTPUT = new TranslationTextComponent(START + "output").withStyle(TextFormatting.BLACK);
    private final boolean[] outputStates = new boolean[AdditionalRedstone.INPUT_STATES.length];
    private int index;

    public TruthtableScreen(ThreeInputLogicGate block) {
        super(null, 192, 168);
        this.setSelectedBlock(block);
    }

    protected void setSelectedBlock(ThreeInputLogicGate block) {
        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            this.outputStates[i] = block.logic.apply(AdditionalRedstone.INPUT_STATES[i][0], AdditionalRedstone.INPUT_STATES[i][1], AdditionalRedstone.INPUT_STATES[i][2]);
        }
        this.index = TruthTableHelper.getIndexOf(block);
        this.setTitle(makeTitle(block.getName()));
    }

    @Override
    protected void init() {
        super.init();
        this.addCancelButton(this.leftPos + 9, this.topPos + 139, 174, 20);
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack); // render texture and transparent background
        super.render(poseStack, mouseX, mouseY, partialTicks); // render buttons and labels

        // render title
        this.font.draw(poseStack, this.getTitle(), this.leftPos + 8, this.topPos + 5, 4210752);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        this.font.draw(poseStack, INPUT_A, x + 1, y, 0);
        this.font.draw(poseStack, INPUT_B, x + 44, y, 0);
        this.font.draw(poseStack, INPUT_C, x + 88, y, 0);
        this.font.draw(poseStack, OUTPUT, x + 133, y, 0);

        // render the input and output states
        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i]; // input states for a, b and c
            boolean output = this.outputStates[i];
            y = topPos + 31 + i * 13;
            // render input states
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? OnOffButton.ON : OnOffButton.OFF);
                this.font.draw(poseStack, input[j] ? OnOffButton.ON : OnOffButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0);
            }
            // render output state
            int width = this.font.width(output ? OnOffButton.ON : OnOffButton.OFF);
            this.font.draw(poseStack, output ? OnOffButton.ON : OnOffButton.OFF, this.leftPos + 141 + (int) (21.5F - width / 2F), y, 0);
        }

        // render tooltip for the hovered tab
        for (int i = 0; i < 6; i++) {
            if (isMouseOverTab(i, mouseX, mouseY)) {
                IFormattableTextComponent tooltip = TruthTableHelper.getAsComponent(i);
                renderTooltip(poseStack, tooltip, mouseX, mouseY);
                break;
            }
        }
        this.renderCustomButtonTooltips(poseStack, mouseX, mouseY);
    }

    @Override
    public void renderBackground(MatrixStack poseStack) {
        super.renderBackground(poseStack);

        // render tabs
        int x = this.leftPos;
        int y = this.topPos - 21;
        for (int i = 0; i < 6; i++) {
            this.minecraft.getItemRenderer().renderGuiItem(TruthTableHelper.getAsItemStack(i), x + 4 + i * 24, y + 3);
            if (i == this.index) {
                continue; // skip rendering the selected tab, will be rendered later
            }
            this.minecraft.getTextureManager().bind(TEXTURE);
            blit(poseStack, x + i * 24, y, 0, 204, 24, 24);
        }

        // render main texture
        this.renderTexture(poseStack, TEXTURE);

        // render selected tab on top of the main texture
        int textureOffset = (this.index == 0) ? 0 : 24; // first tab has a different texture
        this.minecraft.getTextureManager().bind(TEXTURE);
        blit(poseStack, x + this.index * 24, y - 3, textureOffset, 228, 24, 28);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        for (int i = 0; i < 6; i++) {
            if (isMouseOverTab(i, mouseX, mouseY)) {
                if (i == this.index) {
                    break; // do nothing if the same tab is clicked
                }
                this.playButtonClickSound();
                this.setSelectedBlock(TruthTableHelper.get(i));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseKey);
    }

    private boolean isMouseOverTab(int index, double mouseX, double mouseY) {
        int startX = this.leftPos;
        int startY = this.topPos - 24;
        return mouseX >= startX + index * 24 && mouseX < startX + (index + 1) * 24 && mouseY >= startY && mouseY < startY + 24;
    }

    private static IFormattableTextComponent makeTitle(IFormattableTextComponent blockDescription) {
        blockDescription.withStyle(TextFormatting.DARK_BLUE);
        return new TranslationTextComponent(START + "title", blockDescription);
    }

}
