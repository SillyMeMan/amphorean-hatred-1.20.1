package net.vinh.hatred.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.brigadier.arguments.AbilityArgumentType;

public class AbilityCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("ability")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.entities())
                        .then(CommandManager.literal("reset-ability-cooldown")
                                .then(CommandManager.argument("ability", AbilityArgumentType.ability())
                                        .executes(AbilityCommand::runResetCooldown)))
                        .then(CommandManager.literal("reset-cooldowns")
                                .executes(AbilityCommand::runResetAllCooldowns))
                        .then(CommandManager.literal("attempt")
                                .then(CommandManager.argument("ability", AbilityArgumentType.ability())
                                        .executes(AbilityCommand::runAbilityAttempt)))));
    }

    private static int runResetCooldown(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int i = 0;

        for(Entity entity : EntityArgumentType.getEntities(context, "targets")) {
            if(entity instanceof LivingEntity living) {
                living.resetCooldown(AbilityArgumentType.getAbility(context, "ability"));
                i++;
            }
        }

        context.getSource().sendFeedback(() -> Text.literal("The cooldown of " + AbilityArgumentType.getAbility(context, "ability").getName() + " has been reset for selected entities"), false);

        return i;
    }

    private static int runResetAllCooldowns(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int i = 0;

        for(Entity entity : EntityArgumentType.getEntities(context, "targets")) {
            if(entity instanceof LivingEntity living) {
                living.resetAllCooldowns();
                i++;
            }
        }

        context.getSource().sendFeedback(() -> Text.literal("The cooldown of all registered abilities have been reset for selected entities"), false);

        return i;
    }

    private static int runAbilityAttempt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Ability ability = AbilityArgumentType.getAbility(context, "ability");
        int i = 0;

        for(Entity entity : EntityArgumentType.getEntities(context, "targets")) {
            if(entity instanceof LivingEntity living) {
                if(living.attemptAbility(ability).isSuccessful()) i++;
            }
        }

        return i;
    }
}
