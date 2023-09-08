package com.tristankechlo.additionalredstone.client.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ToggleButton extends AbstractButton {

    public static final Component ON = Component.translatable("options.on").withStyle(ChatFormatting.DARK_GREEN);
    public static final Component OFF = Component.translatable("options.off").withStyle(ChatFormatting.DARK_RED);
    public static final Supplier<Tooltip> TOOLTIP_ON = () -> Tooltip.create(ON);
    public static final Supplier<Tooltip> TOOLTIP_OFF = () -> Tooltip.create(OFF);
    private BiConsumer<Integer, Boolean> consumer = null;
    private boolean toggled = false;
    private final int i;

    public ToggleButton(int x, int y, int width, int height, int i) {
        super(x, y, width, height, OFF);
        updateTooltip();
        this.i = i;
    }

    @Override
    public Component getMessage() {
        return this.toggled ? ON : OFF;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }

    @Override
    public void onPress() {
        this.toggled = !this.toggled;
        setToggled(this.toggled);
    }

    private void updateTooltip() {
        setTooltip(this.toggled ? TOOLTIP_ON.get() : TOOLTIP_OFF.get());
    }

    private void updateMessage() {
        setMessage(this.toggled ? ON : OFF);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        updateMessage();
        updateTooltip();
        if (this.consumer != null) {
            this.consumer.accept(this.i, this.toggled);
        }
    }

    public void setConsumer(BiConsumer<Integer, Boolean> consumer) {
        this.consumer = consumer;
    }

}
