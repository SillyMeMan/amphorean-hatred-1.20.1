package net.vinh.hatred.internal.entity;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.internal.ability.state.CombatStates;

public interface PlayerEntityInjectionAccess {
    default void inventoryFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        StatusEffectInstance instance = new StatusEffectInstance(CombatStates.INVENTORY_FREEZE, -1, 0, false, false);

        self.addStatusEffect(instance);
    }

    default void inventoryUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        self.removeStatusEffect(CombatStates.INVENTORY_FREEZE);
    }
}
