package net.vinh.hatred.api.ability;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

@Deprecated
public interface IAbility {
    void alt_ability_1(ServerPlayerEntity player);
    void alt_ability_2(ServerPlayerEntity player);
    void alt_ability_3(ServerPlayerEntity player);
    void alt_ability_4(ServerPlayerEntity player);

    @Deprecated
    class AbilityCooldownManager {
        public static void putOnCooldown(ItemStack stack, int abilityNumber, int durationInTicks) {
            stack.getOrCreateNbt().putInt("AbilityCooldown_" + abilityNumber, durationInTicks);
        }

        public static boolean isOnCooldown(ItemStack stack, int ability) {
            return false;
        }

        public static void tickCooldowns(ItemStack stack) {
            var nbt = stack.getNbt();
            if (nbt == null) return;

            for (int i = 1; i <= 4; i++) {
                String key = "AbilityCooldown_" + i;
                int v = nbt.getInt(key);
                if (v > 0) nbt.putInt(key, v - 1);
            }
        }
    }
}
