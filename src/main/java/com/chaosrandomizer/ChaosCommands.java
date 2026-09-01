package com.chaosrandomizer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ChaosCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    Commands.literal("chaos")
                            .then(Commands.literal("activate").executes(ChaosCommands::activate))
                            .then(Commands.literal("deactivate").executes(ChaosCommands::deactivate))
                            .then(Commands.literal("about").executes(ChaosCommands::about))
                            .then(Commands.literal("info").executes(ChaosCommands::info))
            );
        });
    }

    private static int activate(CommandContext<CommandSourceStack> context) {
        Chaosrandomizer.setActive(true);
        context.getSource().sendSuccess(() -> Component.literal("Chaos Randomizer activated!"), false);
        return 1;
    }

    private static int deactivate(CommandContext<CommandSourceStack> context) {
        Chaosrandomizer.setActive(false);
        context.getSource().sendSuccess(() -> Component.literal("Chaos Randomizer deactivated."), false);
        return 1;
    }

    private static int about(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("Chaos Randomizer v1.0 - Created by Gaucheyy"), false);
        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> context) {
        boolean status = Chaosrandomizer.isActive();
        String message = "Status: " + (status ? "Active" : "Inactive") + " | Total Effects: 3";
        context.getSource().sendSuccess(() -> Component.literal(message), false);
        return 1;
    }
}