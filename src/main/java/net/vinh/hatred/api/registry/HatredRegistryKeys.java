package net.vinh.hatred.api.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;

public class HatredRegistryKeys {
    public static final RegistryKey<Registry<Ability>> ABILITY = of("ability");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier(id));
    }
}
