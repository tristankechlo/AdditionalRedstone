package com.tristankechlo.additionalredstone.client.screen;

import java.time.LocalTime;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import com.tristankechlo.additionalredstone.network.PacketHandler;
import com.tristankechlo.additionalredstone.network.packets.SetTimerValues;
import com.tristankechlo.additionalredstone.tileentity.TimerTileEntity;
import com.tristankechlo.additionalredstone.util.Utils;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TimerScreen extends Screen {

	private static final ITextComponent TITLE = new TranslationTextComponent("screen.additionalredstone.oscillator");
	private static final ResourceLocation ERROR = new ResourceLocation(AdditionalRedstone.MOD_ID,
			"textures/other/icons.png");
	private TextFieldWidget powerUpWidget;
	private TextFieldWidget powerDownWidget;
	private TextFieldWidget intervalWidget;
	private Button saveButton;
	private Button cancelButton;
	private BlockPos pos;
	private int powerUpTime;
	private int powerDownTime;
	private int interval;
	private boolean powerUpError = false;
	private boolean powerDownError = false;
	private boolean intervalError = false;

	public TimerScreen(int powerUp, int powerDown, int interval, BlockPos pos) {
		super(TITLE);
		this.powerUpTime = powerUp;
		this.powerDownTime = powerDown;
		this.interval = interval;
		this.pos = pos;
	}

	@Override
	public void tick() {
		this.powerUpWidget.tick();
		this.powerDownWidget.tick();
		this.intervalWidget.tick();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		this.powerUpWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 60, 98, 20,
				new StringTextComponent("timer_power_up"));
		this.powerDownWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 90, 98, 20,
				new StringTextComponent("timer_power_down"));
		this.intervalWidget = new TextFieldWidget(this.font, this.width / 2 + 32, 120, 98, 20,
				new StringTextComponent("timer_interval"));
		this.children.add(this.powerUpWidget);
		this.children.add(this.powerDownWidget);
		this.children.add(this.intervalWidget);
		this.powerUpWidget.setMaxStringLength(5);
		this.powerDownWidget.setMaxStringLength(5);
		this.intervalWidget.setMaxStringLength(5);
		this.setFocusedDefault(this.powerUpWidget);
		this.powerUpWidget.setFocused2(true);
		this.powerUpWidget.setText(String.valueOf(this.powerUpTime));
		this.powerDownWidget.setText(String.valueOf(this.powerDownTime));
		this.intervalWidget.setText(String.valueOf(this.interval));

		this.saveButton = new Button(this.width / 2 - 110, 160, 100, 20,
				new TranslationTextComponent("screen.additionalredstone.save"), (b) -> {
					this.save();
				});
		this.cancelButton = new Button(this.width / 2 + 10, 160, 100, 20,
				new TranslationTextComponent("screen.additionalredstone.cancel"), (b) -> {
					this.cancel();
				});
		this.addButton(saveButton);
		this.addButton(cancelButton);
	}

	private void save() {
		int powerUp = this.getPowerUpTime();
		int powerDown = this.getPowerDownTime();
		int interval = this.getInterval();
		if (this.powerUpError || this.powerDownError || this.intervalError) {
			return;
		}
		PacketHandler.INSTANCE.sendToServer(new SetTimerValues(powerUp, powerDown, interval, pos));
		this.closeScreen();
	}

	private void cancel() {
		this.closeScreen();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.powerUpWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.powerDownWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.intervalWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		drawCenteredString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.timer.description"), this.width / 2, 30,
				Utils.TEXT_COLOR_SCREEN);

		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.timer.power.on"), this.width / 2 - 130, 65,
				Utils.TEXT_COLOR_SCREEN);
		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.timer.power.off"), this.width / 2 - 130, 95,
				Utils.TEXT_COLOR_SCREEN);
		AbstractGui.drawString(matrixStack, this.font,
				new TranslationTextComponent("screen.additionalredstone.timer.interval"), this.width / 2 - 130, 125,
				Utils.TEXT_COLOR_SCREEN);

		if (this.powerUpError) {
			this.minecraft.getTextureManager().bindTexture(ERROR);
			this.blit(matrixStack, this.width / 2 + 140, 61, 1, 1, 18, 18);
		}
		if (this.powerDownError) {
			this.minecraft.getTextureManager().bindTexture(ERROR);
			this.blit(matrixStack, this.width / 2 + 140, 91, 1, 1, 18, 18);
		}
		if (this.intervalError) {
			this.minecraft.getTextureManager().bindTexture(ERROR);
			this.blit(matrixStack, this.width / 2 + 140, 121, 1, 1, 18, 18);
		}
	}

	private int getPowerUpTime() {
		int powerUp = 0;
		String text = this.powerUpWidget.getText();
		if (text.equalsIgnoreCase("24:00")) {
			text = "00:00";
		}
		try {
			LocalTime time = LocalTime.parse(text);
			powerUp = (time.getHour() * 1000) + (int) (time.getMinute() * 16.67) - 6000;
			if (powerUp < 0) {
				powerUp = 24000 + powerUp;
			}
			this.powerUpError = false;
		} catch (Exception e) {
			try {
				powerUp = Integer.valueOf(text);
			} catch (Exception ex) {
				this.powerUpError = true;
			}
		}
		return MathHelper.clamp(powerUp, TimerTileEntity.minTime, TimerTileEntity.maxTime);
	}

	private int getPowerDownTime() {
		int powerDown = 0;
		String text = this.powerDownWidget.getText();
		if (text.equalsIgnoreCase("24:00")) {
			text = "00:00";
		}
		try {
			LocalTime time = LocalTime.parse(text);
			powerDown = (time.getHour() * 1000) + (int) (time.getMinute() * 16.67) - 6000;
			if (powerDown < 0) {
				powerDown = 24000 + powerDown;
			}
			this.powerDownError = false;
		} catch (Exception e) {
			try {
				powerDown = Integer.valueOf(text);
			} catch (Exception ex) {
				this.powerDownError = true;
			}
		}
		return MathHelper.clamp(powerDown, TimerTileEntity.minTime, TimerTileEntity.maxTime);
	}

	private int getInterval() {
		int interval = 0;
		try {
			interval = Integer.valueOf(this.intervalWidget.getText());
			this.intervalError = false;
		} catch (Exception e) {
			this.intervalError = true;
		}
		return MathHelper.clamp(interval, 1, 1000);
	}

}
