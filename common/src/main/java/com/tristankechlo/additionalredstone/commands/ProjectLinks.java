package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.minecraft.commands.Commands.literal;

public enum ProjectLinks {

    GITHUB("Check out the source code on GitHub: ", "https://github.com/tristankechlo/AdditionalRedstone"),
    ISSUE("If you found an issue, submit it here: ", "https://github.com/tristankechlo/AdditionalRedstone/issues"),
    WIKI("The wiki can be found here: ", "https://github.com/tristankechlo/AdditionalRedstone/wiki"),
    DISCORD("Join the Discord here: ", "https://discord.gg/bhUaWhq"),
    CURSEFORGE("Check out the CurseForge page here: ", "https://curseforge.com/minecraft/mc-mods/additional-redstone"),
    MODRINTH("Check out the Modrinth page here: ", "https://modrinth.com/mod/additional-redstone");

    private final MutableComponent message;

    ProjectLinks(String message, String link) {
        this.message = Component.literal(message);
        this.message.withStyle(ChatFormatting.WHITE);
        this.message.append(clickableLink(link, link));
    }

    public int execute(CommandContext<CommandSourceStack> sender) {
        sender.getSource().sendSuccess(start().append(message), false);
        return 0;
    }

    private static MutableComponent start() {
        return Component.literal("[" + AdditionalRedstone.MOD_NAME + "] ").withStyle(ChatFormatting.GOLD);
    }

    private static MutableComponent clickableLink(String url, String displayText) {
        MutableComponent mutableComponent = Component.literal(displayText);
        mutableComponent.withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE);
        mutableComponent.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        return mutableComponent;
    }

    public static void registerAsCommand(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal(AdditionalRedstone.MOD_ID);
        for (ProjectLinks option : values()) {
            command.then(literal(option.name().toLowerCase()).executes(option::execute));
        }
        dispatcher.register(command);
        AdditionalRedstone.LOGGER.info("Command '/{}' registered", AdditionalRedstone.MOD_ID);
    }

}
