package net.vinh.hatred.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.vinh.hatred.api.ability.IAbility;
import net.vinh.hatred.api.event.ServerAbilityEvents;
import net.vinh.hatred.exception.InvalidAbilityNumberException;
import net.vinh.hatred.networking.packet.CrashS2CPacket;

public final class AmphoreanHatredUtil {
    private AmphoreanHatredUtil() {
        throw new AssertionError("Not supposed to be instantized!");
    }

    public static <T> RegistryEntry<T> getEntryFromKey(World world, RegistryKey<Registry<T>> sourceKey, RegistryKey<T> key) {
        return world.getRegistryManager().get(sourceKey).entryOf(key);
    }

    public static RegistryEntry<DamageType> getEntryFromKey(World world, RegistryKey<DamageType> key) {
        return getEntryFromKey(world, RegistryKeys.DAMAGE_TYPE, key);
    }

    // I don't know why u would even use this method, it's dangerous, ok
    public static void endSomeone(ServerPlayerEntity target) {
        ServerPlayNetworking.send(target, CrashS2CPacket.ID, PacketByteBufs.create());
    }

    @Deprecated
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
