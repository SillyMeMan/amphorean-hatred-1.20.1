package net.vinh.hatred.internal.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.api.ability.state.CombatStates;
import net.vinh.hatred.api.data.Data;

public interface PlayerEntityInjectionAccess {
    default void inventoryFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.INVENTORY_FROZEN, true);
    }

    default void inventoryUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.INVENTORY_FROZEN, false);
    }
}
