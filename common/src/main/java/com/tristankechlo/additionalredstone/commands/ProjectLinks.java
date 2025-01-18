package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ProjectLinks {

    GITHUB("Check out the source code on GitHub: ", "https://github.com/tristankechlo/AdditionalRedstone"),
    ISSUE("If you found an issue, submit it here: ", "https://github.com/tristankechlo/AdditionalRedstone/issues"),
    WIKI("The wiki can be found here: ", "https://github.com/tristankechlo/AdditionalRedstone/wiki"),
    DISCORD("Join the Discord here: ", "https://discord.gg/bhUaWhq"),
    CURSEFORGE("Check out the CurseForge page here: ", "https://curseforge.com/minecraft/mc-mods/additional-redstone"),
    MODRINTH("Check out the Modrinth page here: ", "https://modrinth.com/mod/additional-redstone");

    private final MutableComponent message;
    public static final List<String> ARGS = Stream.of(ProjectLinks.values()).map(e -> e.name().toLowerCase()).collect(Collectors.toList());

    ProjectLinks(String message, String link) {
        this.message = new TextComponent(message);
        this.message.withStyle(ChatFormatting.WHITE);
        this.message.append(clickableLink(link, link));
    }

    public int execute(CommandContext<CommandSourceStack> sender) {
        sender.getSource().sendSuccess(start().append(message), false);
        return 0;
    }

    public static MutableComponent start() {
        return new TextComponent("[" + AdditionalRedstone.MOD_NAME + "] ").withStyle(ChatFormatting.GOLD);
    }

    public static MutableComponent clickableLink(String url, String displayText) {
        MutableComponent mutableComponent = new TextComponent(displayText);
        mutableComponent.withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE);
        mutableComponent.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        return mutableComponent;
    }

}
