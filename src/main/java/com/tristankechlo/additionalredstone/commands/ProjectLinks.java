package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import static net.minecraft.command.Commands.literal;

public enum ProjectLinks {

    GITHUB("Check out the source code on GitHub: ", "https://github.com/tristankechlo/AdditionalRedstone"),
    ISSUE("If you found an issue, submit it here: ", "https://github.com/tristankechlo/AdditionalRedstone/issues"),
    WIKI("The wiki can be found here: ", "https://github.com/tristankechlo/AdditionalRedstone/wiki"),
    DISCORD("Join the Discord here: ", "https://discord.gg/bhUaWhq"),
    CURSEFORGE("Check out the CurseForge page here: ", "https://curseforge.com/minecraft/mc-mods/additional-redstone"),
    MODRINTH("Check out the Modrinth page here: ", "https://modrinth.com/mod/additional-redstone");

    private final IFormattableTextComponent message;

    ProjectLinks(String message, String link) {
        this.message = new StringTextComponent(message);
        this.message.withStyle(TextFormatting.WHITE);
        this.message.append(clickableLink(link, link));
    }

    public int execute(CommandContext<CommandSource> sender) {
        sender.getSource().sendSuccess(start().append(message), false);
        return 0;
    }

    private static IFormattableTextComponent start() {
        return new StringTextComponent("[" + AdditionalRedstone.MOD_NAME + "] ").withStyle(TextFormatting.GOLD);
    }

    private static IFormattableTextComponent clickableLink(String url, String displayText) {
        IFormattableTextComponent mutableComponent = new StringTextComponent(displayText);
        mutableComponent.withStyle(TextFormatting.GREEN, TextFormatting.UNDERLINE);
        mutableComponent.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        return mutableComponent;
    }

    public static void registerAsCommand(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> command = literal(AdditionalRedstone.MOD_ID);
        for (ProjectLinks option : values()) {
            command.then(literal(option.name().toLowerCase()).executes(option::execute));
        }
        event.getDispatcher().register(command);
        AdditionalRedstone.LOGGER.info("Command '/{}' registered", AdditionalRedstone.MOD_ID);
    }

}
