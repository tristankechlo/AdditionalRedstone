package com.tristankechlo.additionalredstone.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.tristankechlo.additionalredstone.AdditionalRedstone;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public final class ModCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal(AdditionalRedstone.MOD_ID)
                .then(literal("github").executes(ProjectLinks.GITHUB::execute))
                .then(literal("issue").executes(ProjectLinks.ISSUE::execute))
                .then(literal("wiki").executes(ProjectLinks.WIKI::execute))
                .then(literal("discord").executes(ProjectLinks.DISCORD::execute))
                .then(literal("curseforge").executes(ProjectLinks.CURSEFORGE::execute))
                .then(literal("modrinth").executes(ProjectLinks.MODRINTH::execute));
        dispatcher.register(command);
        AdditionalRedstone.LOGGER.info("Command '/{}' registered", AdditionalRedstone.MOD_ID);
    }

}