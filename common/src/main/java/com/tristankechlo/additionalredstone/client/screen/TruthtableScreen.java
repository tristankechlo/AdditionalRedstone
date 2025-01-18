package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.OnOffButton;
import com.tristankechlo.additionalredstone.client.util.TruthTableHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class TruthtableScreen extends CustomScreen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/gui/truthtable.png");
    private static final String START = "screen.additionalredstone.truthtable.";
    public static final MutableComponent INPUT_A = new TranslatableComponent(START + "input_a").withStyle(ChatFormatting.BLACK);
    public static final MutableComponent INPUT_B = new TranslatableComponent(START + "input_b").withStyle(ChatFormatting.BLACK);
    public static final MutableComponent INPUT_C = new TranslatableComponent(START + "input_c").withStyle(ChatFormatting.BLACK);
    public static final MutableComponent OUTPUT = new TranslatableComponent(START + "output").withStyle(ChatFormatting.BLACK);
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
        Button cancelButton = new Button(this.leftPos + 9, this.topPos + 139, 174, 20, TEXT_CANCEL, (b) -> this.onClose());
        this.addRenderableWidget(cancelButton);
    }

    @Override
    public void render(PoseStack graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics); // render texture and transparent background
        super.render(graphics, mouseX, mouseY, partialTicks); // render buttons and labels

        // render title
        drawString(graphics, this.font, this.getTitle(), this.leftPos + 8, this.topPos + 5, 4210752);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        drawString(graphics, this.font, INPUT_A, x + 1, y, 0);
        drawString(graphics, this.font, INPUT_B, x + 44, y, 0);
        drawString(graphics, this.font, INPUT_C, x + 88, y, 0);
        drawString(graphics, this.font, OUTPUT, x + 133, y, 0);

        // render the input and output states
        for (int i = 0; i < AdditionalRedstone.INPUT_STATES.length; i++) {
            boolean[] input = AdditionalRedstone.INPUT_STATES[i]; // input states for a, b and c
            boolean output = this.outputStates[i];
            y = topPos + 31 + i * 13;
            // render input states
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? OnOffButton.ON : OnOffButton.OFF);
                drawString(graphics, this.font, input[j] ? OnOffButton.ON : OnOffButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0);
            }
            // render output state
            int width = this.font.width(output ? OnOffButton.ON : OnOffButton.OFF);
            drawString(graphics, this.font, output ? OnOffButton.ON : OnOffButton.OFF, this.leftPos + 141 + (int) (21.5F - width / 2F), y, 0);
        }

        // render tooltip for the hovered tab
        for (int i = 0; i < 6; i++) {
            if (isMouseOverTab(i, mouseX, mouseY)) {
                MutableComponent tooltip = TruthTableHelper.getAsComponent(i);
                renderTooltip(graphics, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    public void renderBackground(PoseStack graphics) {
        super.renderBackground(graphics);

        // render tabs
        int x = this.leftPos;
        int y = this.topPos - 21;
        for (int i = 0; i < 6; i++) {
            this.minecraft.getItemRenderer().renderGuiItem(TruthTableHelper.getAsItemStack(i), x + 4 + i * 24, y + 3);
            if (i == this.index) {
                continue; // skip rendering the selected tab, will be rendered later
            }
            RenderSystem.setShaderTexture(0, TEXTURE);
            blit(graphics, x + i * 24, y, 0, 204, 24, 24);
        }

        // render main texture
        this.renderTexture(graphics, TEXTURE);

        // render selected tab on top of the main texture
        int textureOffset = (this.index == 0) ? 0 : 24; // first tab has a different texture
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(graphics, x + this.index * 24, y - 3, textureOffset, 228, 24, 28);
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

    private static MutableComponent makeTitle(MutableComponent blockDescription) {
        blockDescription.withStyle(ChatFormatting.DARK_BLUE);
        return new TranslatableComponent(START + "title", blockDescription);
    }

}
