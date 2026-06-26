package net.vinh.hatred.api.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.HatredInternalAttachments;
import net.vinh.hatred.internal.ability.AbstractAbility;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public final class Cooldowns {
    public static void resetCooldown(LivingEntity entity, Identifier ability) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredInternalAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        map.remove(ability);
        shit.remove(ability);

        Data.API.set(entity, HatredInternalAttachments.ABILITY_COOLDOWNS, map);
        Data.API.set(entity, HatredInternalAttachments.PRECASTS, shit);
    }

    public static void resetAllCooldown(LivingEntity entity) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredInternalAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        map.clear();
        shit.clear();

        Data.API.set(entity, HatredInternalAttachments.ABILITY_COOLDOWNS, map);
        Data.API.set(entity, HatredInternalAttachments.PRECASTS, shit);
    }

    public static boolean isReady(LivingEntity entity, Identifier ability) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredInternalAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        CooldownEntry entry = map.get(ability);

        if(shit.containsKey(ability)) {
            return false;
        }

        if(Data.API.get(entity, HatredInternalAttachments.IS_USING_ABILITY) && !Data.API.get(entity, HatredInternalAttachments.NO_STUN)) {
            return false;
        }

        if(entry != null) {
            return entry.readyTick <= entity.getServer().getTicks();
        }

        return true;
    }

    public static void setCooldown(LivingEntity entity, Identifier ability, long cooldown) {
        assert entity.getServer() != null;
        @Nullable Identifier cooldownGroup = HatredRegistries.ABILITY.get(ability).cooldownGroup();

        for(Identifier id : HatredRegistries.ABILITY.getIds()) {
            if(cooldownGroup == null) {
                Map<Identifier, CooldownEntry> map =
                        Data.API.get(entity, HatredInternalAttachments.ABILITY_COOLDOWNS);

                CooldownEntry entry = map.get(ability);

                if (entry == null) {
                    entry = new CooldownEntry(
                            entity.getServer().getTicks() + cooldown
                    );

                    map.put(ability, entry);
                }

                entry.readyTick = entity.getServer().getTicks() + cooldown;

                Data.API.set(entity, HatredInternalAttachments.ABILITY_COOLDOWNS, map);

                return;
            }

            if(cooldownGroup.equals(Objects.requireNonNull(HatredRegistries.ABILITY.get(id)).cooldownGroup())) {
                Map<Identifier, CooldownEntry> map =
                        Data.API.get(entity, HatredInternalAttachments.ABILITY_COOLDOWNS);

                CooldownEntry entry = map.get(id);

                if (entry == null) {
                    entry = new CooldownEntry(
                            entity.getServer().getTicks() + cooldown
                    );

                    map.put(id, entry);
                }

                entry.readyTick = entity.getServer().getTicks() + cooldown;

                Data.API.set(entity, HatredInternalAttachments.ABILITY_COOLDOWNS, map);
            }
        }
    }
}
