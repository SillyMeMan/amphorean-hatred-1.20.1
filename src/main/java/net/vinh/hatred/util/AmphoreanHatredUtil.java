package net.vinh.hatred.util;

import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.api.ability.IAbility;
import net.vinh.hatred.api.event.ServerAbilityEvents;
import net.vinh.hatred.exception.InvalidAbilityNumberException;

public class AmphoreanHatredUtil {
    public static void useAbility(ServerPlayerEntity player, int abilityNumber) {
        if(!ServerAbilityEvents.USE_ABILITY.invoker().useAbility(player, abilityNumber)) {
            return;
        }

        Item item = player.getMainHandStack().getItem();

        if(item instanceof IAbility abilityItem) {
            switch (abilityNumber) {
                case 1 -> {
                    if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 1)) {
                        abilityItem.alt_ability_1(player);
                    }
                }
                case 2 -> {
                    if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 2)) {
                        abilityItem.alt_ability_2(player);
                    }
                }
                case 3 -> {
                    if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 3)) {
                        abilityItem.alt_ability_3(player);
                    }
                }
                case 4 -> {
                    if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 4)) {
                        abilityItem.alt_ability_4(player);
                    }
                }
                default -> throw new InvalidAbilityNumberException();
            }
        }
    }
}
