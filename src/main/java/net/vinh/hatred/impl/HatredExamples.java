package net.vinh.hatred.impl;

import net.minecraft.registry.Registry;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.registry.HatredRegistries;

import static net.vinh.hatred.AmphoreanHatred.id;

public class HatredExamples {
    public static Ability EXAMPLE_ABILITY;

    public static void initExamples() {
        EXAMPLE_ABILITY = Registry.register(HatredRegistries.ABILITY, id("example_ability"), new ExampleAbility());
    }
}
