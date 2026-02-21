package net.vinh.hatred.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ServerAbilityEvents {
    public static final Event<UseAbility> USE_ABILITY = EventFactory.createArrayBacked(UseAbility.class, callbacks -> (player, abilityNumber) -> {
        for(UseAbility callback : callbacks) {
            if(!callback.useAbility(player, abilityNumber)) {
                return false;
            }
        }

        return true;
    });

    @FunctionalInterface
    public interface UseAbility {
        boolean useAbility(ServerPlayerEntity player, int abilityNumber);
    }
}
