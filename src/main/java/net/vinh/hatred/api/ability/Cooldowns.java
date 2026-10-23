package net.vinh.hatred.api.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.cca.HatredLivingEntityComponent;
import net.vinh.hatred.cca.ModComponents;

import java.util.Map;
import java.util.Objects;

public final class Cooldowns {
    public static void resetCooldown(LivingEntity entity, Identifier ability, boolean includeCooldownGroup) {
        Map<Identifier, CooldownEntry> cooldowns =
                ModComponents.LIVING_ENTITY.get(entity).getCooldowns();

        if (!includeCooldownGroup) {
            cooldowns.remove(ability);
        } else {
            Ability target = Objects.requireNonNull(
                    HatredRegistries.ABILITY.get(ability),
                    "Unknown ability: " + ability
            );

            Identifier group = target.cooldownGroup();

            if (group == null) {
                cooldowns.remove(ability);
            } else {
                for (Identifier id : HatredRegistries.ABILITY.getIds()) {
                    Ability candidate = HatredRegistries.ABILITY.get(id);

                    if (candidate != null
                            && group.equals(candidate.cooldownGroup())) {
                        cooldowns.remove(id);
                    }
                }
            }
        }

        ModComponents.LIVING_ENTITY.sync(entity);
    }

    public static void resetAllCooldown(LivingEntity entity) {
        ModComponents.LIVING_ENTITY.get(entity)
                .getCooldowns()
                .clear();
        ModComponents.LIVING_ENTITY.sync(entity);
    }

    public static boolean isReady(LivingEntity entity, Identifier ability) {
        assert entity.getServer() != null;

        HatredLivingEntityComponent component = ModComponents.LIVING_ENTITY.get(entity);

        Map<Identifier, CooldownEntry> cooldowns = component.getCooldowns();

        if (component.isUsingAbility && !component.noStun) {
            return false;
        }

        CooldownEntry entry = cooldowns.get(ability);

        return entry == null || entry.readyTick <= entity.getServer().getTicks();
    }

    public static void setCooldown(LivingEntity entity, Identifier ability, long cooldown) {
        assert entity.getServer() != null;

        Ability target = Objects.requireNonNull(
                HatredRegistries.ABILITY.get(ability),
                "Unknown ability: " + ability
        );

        Identifier group = target.cooldownGroup();

        Map<Identifier, CooldownEntry> cooldowns = ModComponents.LIVING_ENTITY.get(entity).getCooldowns();

        long readyTick = entity.getServer().getTicks() + cooldown;

        if (group == null) {
            setCooldownEntry(cooldowns, ability, readyTick);
            ModComponents.LIVING_ENTITY.sync(entity);
            return;
        }

        for (Identifier id : HatredRegistries.ABILITY.getIds()) {
            Ability candidate = HatredRegistries.ABILITY.get(id);

            if (candidate != null && group.equals(candidate.cooldownGroup())) {
                setCooldownEntry(cooldowns, id, readyTick);
            }
        }

        ModComponents.LIVING_ENTITY.sync(entity);
    }

    private static void setCooldownEntry(
            Map<Identifier, CooldownEntry> cooldowns,
            Identifier ability,
            long readyTick
    ) {
        CooldownEntry entry = cooldowns.get(ability);

        if (entry == null) {
            cooldowns.put(ability, new CooldownEntry(readyTick));
        } else {
            entry.readyTick = readyTick;
        }
    }
}
