package net.vinh.hatred.internal.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.cca.ModComponents;

public interface PlayerEntityInjectionAccess {
    default void inventoryFreeze() {
        PlayerEntity self = (PlayerEntity) this;

        ModComponents.PLAYER.get(self).inventoryFrozen = true;
        ModComponents.PLAYER.sync(self);
    }

    default void inventoryUnfreeze() {
        PlayerEntity self = (PlayerEntity) this;

        ModComponents.PLAYER.get(self).inventoryFrozen = false;
        ModComponents.PLAYER.sync(self);
    }
}
