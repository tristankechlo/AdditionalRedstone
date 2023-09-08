package com.tristankechlo.additionalredstone.client.screen;

import com.tristankechlo.additionalredstone.Constants;
import com.tristankechlo.additionalredstone.blocks.ThreeInputLogicGate;
import com.tristankechlo.additionalredstone.client.util.CustomScreen;
import com.tristankechlo.additionalredstone.client.util.OnOffButton;
import com.tristankechlo.additionalredstone.client.util.TruthTableHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class TruthtableScreen extends CustomScreen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/truthtable.png");
    private static final String START = "screen.additionalredstone.truthtable.";
    public static final Component INPUT_A = Component.translatable(START + "input_a").withStyle(ChatFormatting.BLACK);
    public static final Component INPUT_B = Component.translatable(START + "input_b").withStyle(ChatFormatting.BLACK);
    public static final Component INPUT_C = Component.translatable(START + "input_c").withStyle(ChatFormatting.BLACK);
    public static final Component OUTPUT = Component.translatable(START + "output").withStyle(ChatFormatting.BLACK);
    private final boolean[] outputStates = new boolean[Constants.INPUT_STATES.length];
    private int index;

    public TruthtableScreen(ThreeInputLogicGate block) {
        super(null, 192, 168);
        this.setSelectedBlock(block);
    }

    protected void setSelectedBlock(ThreeInputLogicGate block) {
        for (int i = 0; i < Constants.INPUT_STATES.length; i++) {
            this.outputStates[i] = block.logic.apply(Constants.INPUT_STATES[i][0], Constants.INPUT_STATES[i][1], Constants.INPUT_STATES[i][2]);
        }
        this.index = TruthTableHelper.getIndexOf(block);
        this.setTitle(makeTitle(block.getName()));
    }

    @Override
    protected void init() {
        super.init();

        Button cancelButton = new Button.Builder(CustomScreen.TEXT_CLOSE, (b) -> this.onClose())
                .bounds(this.leftPos + 9, this.topPos + 139, 174, 20)
                .tooltip(CustomScreen.TOOLTIP_CLOSE.get()).build();
        this.addRenderableWidget(cancelButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics); // render texture and transparent background
        super.render(graphics, mouseX, mouseY, partialTicks); // render buttons and labels

        // render title
        graphics.drawString(this.font, this.getTitle(), this.leftPos + 8, this.topPos + 5, 4210752, false);

        // render the input and output labels
        int x = this.leftPos + 12;
        int y = this.topPos + 18;
        graphics.drawString(this.font, INPUT_A, x + 1, y, 0, false);
        graphics.drawString(this.font, INPUT_B, x + 44, y, 0, false);
        graphics.drawString(this.font, INPUT_C, x + 88, y, 0, false);
        graphics.drawString(this.font, OUTPUT, x + 133, y, 0, false);

        // render the input and output states
        for (int i = 0; i < Constants.INPUT_STATES.length; i++) {
            boolean[] input = Constants.INPUT_STATES[i]; // input states for a, b and c
            boolean output = this.outputStates[i];
            y = topPos + 31 + i * 13;
            // render input states
            for (int j = 0; j < input.length; j++) {
                int width = this.font.width(input[j] ? OnOffButton.ON : OnOffButton.OFF);
                graphics.drawString(this.font, input[j] ? OnOffButton.ON : OnOffButton.OFF, x + j * 43 + (int) (21F - width / 2F), y, 0, false);
            }
            // render output state
            int width = this.font.width(output ? OnOffButton.ON : OnOffButton.OFF);
            graphics.drawString(this.font, output ? OnOffButton.ON : OnOffButton.OFF, this.leftPos + 141 + (int) (21.5F - width / 2F), y, 0, false);
        }

        // render tooltip for the hovered tab
        for (int i = 0; i < 6; i++) {
            if (isMouseOverTab(i, mouseX, mouseY)) {
                MutableComponent tooltip = TruthTableHelper.getAsComponent(i);
                graphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);

        // render tabs
        int x = this.leftPos;
        int y = this.topPos - 21;
        for (int i = 0; i < 6; i++) {
            graphics.renderItem(TruthTableHelper.getAsItemStack(i), x + 4 + i * 24, y + 3);
            if (i == this.index) {
                continue; // skip rendering the selected tab, will be rendered later
            }
            graphics.blit(TEXTURE, x + i * 24, y, 0, 204, 24, 24);
        }

        // render main texture
        this.renderTexture(graphics, TEXTURE);

        // render selected tab on top of the main texture
        int textureOffset = (this.index == 0) ? 0 : 24; // first tab has a different texture
        graphics.blit(TEXTURE, x + this.index * 24, y - 3, textureOffset, 228, 24, 28);
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

    private static Component makeTitle(MutableComponent blockDescription) {
        blockDescription.withStyle(ChatFormatting.DARK_BLUE);
        return Component.translatable(START + "title", blockDescription);
    }

}
