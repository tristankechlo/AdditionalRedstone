package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.client.ClientSetup;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OscillatorScreen extends Screen {

    private static final Component TITLE = Component.translatable("screen.additionalredstone.oscillator");
    private static final ResourceLocation ICONS = new ResourceLocation(AdditionalRedstone.MOD_ID, "textures/other/icons.png");
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
    public boolean isPauseScreen() {
        // TODO adjustable via config
        return false;
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
        this.ticksOnWidget.setFocus(true);
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
        PacketHandler.INSTANCE.sendToServer(new SetOscillatorValues(ticks_on, ticks_off, pos));
        this.onClose();
    }

    private void cancel() {
        this.onClose();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.ticksOnWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        this.ticksOffWidget.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font,
                Component.translatable("screen.additionalredstone.oscillator.description"), this.width / 2, 30,
                ClientSetup.TEXT_COLOR_SCREEN);

        GuiComponent.drawString(matrixStack, this.font,
                Component.translatable("screen.additionalredstone.oscillator.ticks.on"), this.width / 2 - 130, 65,
                ClientSetup.TEXT_COLOR_SCREEN);
        GuiComponent.drawString(matrixStack, this.font,
                Component.translatable("screen.additionalredstone.oscillator.ticks.off"), this.width / 2 - 130, 95,
                ClientSetup.TEXT_COLOR_SCREEN);

        if (this.ticksOnError) {
            RenderSystem.setShaderTexture(0, ICONS);
            this.blit(matrixStack, this.width / 2 + 140, 61, 1, 1, 18, 18);
        }
        if (this.ticksOffError) {
            RenderSystem.setShaderTexture(0, ICONS);
            this.blit(matrixStack, this.width / 2 + 140, 91, 1, 1, 18, 18);
        }

    }

}
