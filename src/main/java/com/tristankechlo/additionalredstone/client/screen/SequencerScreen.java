package com.tristankechlo.additionalredstone.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetSequencerValues;
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
public class SequencerScreen extends Screen {

	private static final ITextComponent TITLE = new TranslationTextComponent("screen.additionalredstone.sequencer");
	private static final ResourceLocation ERROR = new ResourceLocation(AdditionalRedstone.MOD_ID,
			"textures/other/icons.png");
	private TextFieldWidget intervalWidget;
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
	public boolean isPauseScreen() {
		// TODO adjustable via config
		return false;
	}

	@Override
	protected void init() {
		super.init();
		this.intervalWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 60, 98, 20,
				new StringTextComponent("sequencer_interval"));
		this.children.add(this.intervalWidget);
		this.intervalWidget.setMaxStringLength(10);
		this.setFocusedDefault(this.intervalWidget);
		this.intervalWidget.setFocused2(true);
		this.intervalWidget.setText(String.valueOf(this.interval));

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
		int interval = 0;
		try {
			interval = Integer.valueOf(this.intervalWidget.getText());
			this.intervalError = false;
		} catch (Exception e) {
			this.intervalError = true;
		}
		if (this.intervalError) {
			return;
		}
		PacketHandler.INSTANCE.sendToServer(new SetSequencerValues(interval, pos));
		this.closeScreen();
	}

	private void cancel() {
		this.closeScreen();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.intervalWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		drawCenteredString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.sequencer.description"), this.width / 2, 30,
				Utils.TEXT_COLOR_SCREEN);

		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.sequencer.interval"), this.width / 2 - 130, 65,
				Utils.TEXT_COLOR_SCREEN);

		if (this.intervalError) {
			this.minecraft.getTextureManager().bindTexture(ERROR);
			this.blit(matrixStack, this.width / 2 + 140, 61, 1, 1, 18, 18);
		}

	}

}
