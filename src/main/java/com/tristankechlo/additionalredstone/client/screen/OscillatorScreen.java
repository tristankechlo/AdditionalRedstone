package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetOscillatorValues;
import com.tristankechlo.additionalredstone.util.Utils;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OscillatorScreen extends Screen {

	private static final ITextComponent TITLE = new TranslationTextComponent("screen.additionalredstone.oscillator");
	private static final ResourceLocation ICONS = new ResourceLocation(AdditionalRedstone.MOD_ID,
			"textures/other/icons.png");
	private TextFieldWidget ticksOnWidget;
	private TextFieldWidget ticksOffWidget;
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
		this.ticksOnWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 60, 98, 20,
				new StringTextComponent("oscillator_ticks_on"));
		this.ticksOffWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 90, 98, 20,
				new StringTextComponent("oscillator_ticks_off"));
		this.children.add(this.ticksOnWidget);
		this.children.add(this.ticksOffWidget);
		this.ticksOnWidget.setMaxLength(10);
		this.ticksOffWidget.setMaxLength(10);
		this.setInitialFocus(this.ticksOnWidget);
		this.ticksOnWidget.setFocus(true);
		this.ticksOnWidget.setValue(String.valueOf(this.ticksOn));
		this.ticksOffWidget.setValue(String.valueOf(this.ticksOff));

		this.saveButton = new Button(this.width / 2 - 110, 150, 100, 20,
				new TranslationTextComponent("screen.additionalredstone.save"), (b) -> {
					this.save();
				});
		this.cancelButton = new Button(this.width / 2 + 10, 150, 100, 20,
				new TranslationTextComponent("screen.additionalredstone.cancel"), (b) -> {
					this.cancel();
				});
		this.addButton(saveButton);
		this.addButton(cancelButton);
	}

	private void save() {
		int ticks_on = 0;
		int ticks_off = 0;
		try {
			ticks_on = Integer.valueOf(this.ticksOnWidget.getValue());
			this.ticksOnError = false;
		} catch (Exception e) {
			this.ticksOnError = true;
		}
		try {
			ticks_off = Integer.valueOf(this.ticksOffWidget.getValue());
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
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.ticksOnWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.ticksOffWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		drawCenteredString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.oscillator.description"), this.width / 2, 30,
				Utils.TEXT_COLOR_SCREEN);

		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.oscillator.ticks.on"), this.width / 2 - 130, 65,
				Utils.TEXT_COLOR_SCREEN);
		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.oscillator.ticks.off"), this.width / 2 - 130,
				95, Utils.TEXT_COLOR_SCREEN);

		if (this.ticksOnError) {
			this.minecraft.getTextureManager().bind(ICONS);
			this.blit(matrixStack, this.width / 2 + 140, 61, 1, 1, 18, 18);
		}
		if (this.ticksOffError) {
			this.minecraft.getTextureManager().bind(ICONS);
			this.blit(matrixStack, this.width / 2 + 140, 91, 1, 1, 18, 18);
		}

	}

}
