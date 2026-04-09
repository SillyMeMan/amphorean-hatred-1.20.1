package net.vinh.hatred.api.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.registry.HatredRegistries;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.vinh.hatred.AmphoreanHatred.id;

public class AbilityArgumentType implements ArgumentType<Ability> {
    private static final Collection<String> EXAMPLES = List.of("fireball", "dash");

    protected AbilityArgumentType() {}

    public static AbilityArgumentType ability() {
        return new AbilityArgumentType();
    }

    public static <S> Ability getAbility(CommandContext<S> context, String name) {
        return context.getArgument(name, Ability.class);
    }

    @Override
    public Ability parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.fromCommandInput(reader);
        Ability ability = HatredRegistries.ABILITY.get(id);
        if (ability == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
        }

        return ability;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(Identifier id : HatredRegistries.ABILITY.getIds()) {
            builder.suggest(id.toString());
        }
        return builder.buildFuture();
    }

    public static void init() {
        ArgumentTypeRegistry.registerArgumentType(id("ability"), AbilityArgumentType.class, ConstantArgumentSerializer.of(AbilityArgumentType::new));
    }
}
