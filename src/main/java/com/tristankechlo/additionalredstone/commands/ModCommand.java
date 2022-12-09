package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import static net.minecraft.commands.Commands.literal;

public final class ModCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal(AdditionalRedstone.MOD_ID)
                .then(literal("github").executes(ModCommand::github))
                .then(literal("issue").executes(ModCommand::issue))
                .then(literal("wiki").executes(ModCommand::wiki))
                .then(literal("discord").executes(ModCommand::discord))
                .then(literal("curseforge").executes(ModCommand::curseforge))
                .then(literal("modrinth").executes(ModCommand::modrinth));
        dispatcher.register(command);
        AdditionalRedstone.LOGGER.info("Command '/{}' registered", AdditionalRedstone.MOD_ID);
    }

    private static int github(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_URL);
        Component message = new TextComponent("Check out the source code on GitHub: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int issue(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_ISSUE_URL);
        Component message = new TextComponent("If you found an issue, submit it here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int wiki(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.GITHUB_WIKI_URL);
        Component message = new TextComponent("The wiki can be found here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int discord(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.DISCORD_URL);
        Component message = new TextComponent("Join the Discord here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int curseforge(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.CURSEFORGE_URL);
        Component message = new TextComponent("Check out the CurseForge page here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

    private static int modrinth(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Component link = ResponseHelper.clickableLink(AdditionalRedstone.MODRINTH_URL);
        Component message = new TextComponent("Check out the Modrinth page here: ").withStyle(ChatFormatting.WHITE).append(link);
        ResponseHelper.sendMessage(source, message, false);
        return 1;
    }

}
