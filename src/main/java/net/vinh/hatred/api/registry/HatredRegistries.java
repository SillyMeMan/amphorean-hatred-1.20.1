package net.vinh.hatred.api.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.client.animation.AnimationDefinition;
import org.jetbrains.annotations.ApiStatus;

public class HatredRegistries {
    public static final Registry<Ability> ABILITY = FabricRegistryBuilder.createSimple(HatredRegistryKeys.ABILITY).buildAndRegister();
    public static final Registry<AnimationDefinition> ANIMATION = FabricRegistryBuilder.createSimple(HatredRegistryKeys.ANIMATION).buildAndRegister();

    @ApiStatus.Internal
    public static void initRegistries() {}

    private HatredRegistries() {}
}
