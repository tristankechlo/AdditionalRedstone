package com.tristankechlo.additionalredstone.commands;

import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public final class ResponseHelper {

    public static IFormattableTextComponent start() {
        return new StringTextComponent("[" + AdditionalRedstone.MOD_NAME + "] ").withStyle(TextFormatting.GOLD);
    }

    public static void sendMessage(CommandSource source, IFormattableTextComponent message, boolean broadcastToOps) {
        IFormattableTextComponent start = start().append(message);
        source.sendSuccess(start, broadcastToOps);
    }

    public static IFormattableTextComponent clickableLink(String url, String displayText) {
        IFormattableTextComponent mutableComponent = new StringTextComponent(displayText);
        mutableComponent.withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE);
        mutableComponent.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        return mutableComponent;
    }

    public static IFormattableTextComponent clickableLink(String url) {
        return clickableLink(url, url);
    }

}
