package net.vinh.hatred.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.ability.AbilityResult;

public final class ServerAbilityEvents {
    public static final Event<PreCast> PRE_CAST = EventFactory.createArrayBacked(PreCast.class, callbacks -> (caster, ability) -> {
        for(PreCast callback : callbacks) {
            return callback.preCast(caster, ability);
        }

        return AbilityResult.PASS;
    });

    @Deprecated
    public static final Event<UseAbility> USE_ABILITY = EventFactory.createArrayBacked(UseAbility.class, callbacks -> (player, abilityNumber) -> {
        for(UseAbility callback : callbacks) {
            if(!callback.useAbility(player, abilityNumber)) {
                return false;
            }
        }

        return true;
    });

    @Deprecated
    @FunctionalInterface
    public interface UseAbility {
        boolean useAbility(ServerPlayerEntity player, int abilityNumber);
    }

    @FunctionalInterface
    public interface PreCast {
        AbilityResult preCast(LivingEntity caster, Ability ability);
    }
}
