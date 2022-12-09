package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import static net.minecraft.command.Commands.literal;

public final class ModCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> command = literal(AdditionalRedstone.MOD_ID)
                .then(literal("github").executes(ModCommand::github))
                .then(literal("issue").executes(ModCommand::issue))
                .then(literal("wiki").executes(ModCommand::wiki))
                .then(literal("discord").executes(ModCommand::discord))
                .then(literal("curseforge").executes(ModCommand::curseforge))
                .then(literal("modrinth").executes(ModCommand::modrinth));
        dispatcher.register(command);
        AdditionalRedstone.LOGGER.info("Command '/{}' registered", AdditionalRedstone.MOD_ID);
    }

    private static int github(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_URL);
        IFormattableTextComponent message = new StringTextComponent("Check out the source code on GitHub: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int issue(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_ISSUE_URL);
        IFormattableTextComponent message = new StringTextComponent("If you found an issue, submit it here: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int wiki(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_WIKI_URL);
        IFormattableTextComponent message = new StringTextComponent("The wiki can be found here: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int discord(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.DISCORD_URL);
        IFormattableTextComponent message = new StringTextComponent("Join the Discord here: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int curseforge(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.CURSEFORGE_URL);
        IFormattableTextComponent message = new StringTextComponent("Check out the CurseForge page here: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int modrinth(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        IFormattableTextComponent link = ResponseHelper.clickableLink(AdditionalRedstone.MODRINTH_URL);
        IFormattableTextComponent message = new StringTextComponent("Check out the Modrinth page here: ").withStyle(TextFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

}