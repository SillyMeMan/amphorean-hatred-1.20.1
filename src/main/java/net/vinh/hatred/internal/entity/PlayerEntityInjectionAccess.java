package net.vinh.hatred.internal.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.api.ability.state.CombatStates;
import net.vinh.hatred.api.data.Data;

public interface PlayerEntityInjectionAccess {
    default void movementFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.MOVEMENT_FROZEN, true);
    }

    default void movementUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.MOVEMENT_FROZEN, false);
    }

    default void inventoryFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.INVENTORY_FROZEN, true);
    }

    default void inventoryUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.INVENTORY_FROZEN, false);
    }

    default void lockRotation() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.ROTATION_LOCKED, true);
    }

    default void unlockRotation() {
        PlayerEntity self = (PlayerEntity) this;

        Data.API.set(self, CombatStates.ROTATION_LOCKED, false);
    }

    default void completeFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        self.movementFreeze();
        self.lockRotation();
        self.inventoryFreeze();
    }

    default void completeUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        self.movementUnfreeze();
        self.unlockRotation();
        self.inventoryUnfreeze();
    }
}
