package net.vinh.hatred.api.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.client.animation.AnimationDefinition;

public class HatredRegistryKeys {
    public static final RegistryKey<Registry<Ability>> ABILITY = of("ability");
    public static final RegistryKey<Registry<AnimationDefinition>> ANIMATION = of("animation");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier(id));
    }

    private HatredRegistryKeys() {}
}
