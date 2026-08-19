package net.vinh.hatred.api.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import net.vinh.hatred.api.ability.Ability;
import org.jetbrains.annotations.ApiStatus;

public class HatredRegistries {
    public static Registry<Ability> ABILITY = FabricRegistryBuilder.createSimple(HatredRegistryKeys.ABILITY).buildAndRegister();

    @ApiStatus.Internal
    public static void initRegistries() {}
}
