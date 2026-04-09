package net.vinh.hatred.api.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.HatredAttachments;
import net.vinh.hatred.internal.ability.AbstractAbility;

import java.util.List;
import java.util.Map;

public final class Cooldowns {
    public static void resetCooldown(LivingEntity entity, Identifier ability) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        map.remove(ability);
        shit.remove(ability);

        Data.API.set(entity, HatredAttachments.ABILITY_COOLDOWNS, map);
        Data.API.set(entity, HatredAttachments.PRECASTS, shit);
    }

    public static void resetAllCooldown(LivingEntity entity) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        map.clear();
        shit.clear();

        Data.API.set(entity, HatredAttachments.ABILITY_COOLDOWNS, map);
        Data.API.set(entity, HatredAttachments.PRECASTS, shit);
    }

    public static boolean isReady(LivingEntity entity, Identifier ability) {
        Map<Identifier, CooldownEntry> map =
                Data.API.get(entity, HatredAttachments.ABILITY_COOLDOWNS);
        Map<Identifier, AbstractAbility.PreCastInstance> shit =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        CooldownEntry entry = map.get(ability);

        long now = entity.getServer().getTicks();

        if(shit.containsKey(ability)) {
            return false;
        }

        if (entry == null) {
            return true;
        }

        if (entry.charges > 0) {
            return true;
        }

        if (now >= entry.readyTick) {
            entry.charges = entry.maxCharges;
            return true;
        }

        return false;
    }

    public static void setCharges(LivingEntity entity, Identifier ability, int charges) {
        Identifier cooldownGroup = HatredRegistries.ABILITY.get(ability).cooldownGroup();

        for(Identifier id : HatredRegistries.ABILITY.getIds()) {
            if(HatredRegistries.ABILITY.get(id).cooldownGroup() == cooldownGroup) {
                Map<Identifier, CooldownEntry> map =
                        Data.API.get(entity, HatredAttachments.ABILITY_COOLDOWNS);

                CooldownEntry entry = map.get(id);

                if(charges <= 0) {
                    entry = new CooldownEntry(
                            entity.getServer().getTicks() + HatredRegistries.ABILITY.get(id).cooldown(),
                            0,
                            HatredRegistries.ABILITY.get(ability).maxCharges()
                    );
                    map.put(id, entry);

                    Data.API.set(entity, HatredAttachments.ABILITY_COOLDOWNS, map);
                    return;
                }

                if (entry == null) {
                    entry = new CooldownEntry(
                            entity.getServer().getTicks(),
                            charges,
                            HatredRegistries.ABILITY.get(ability).maxCharges()
                    );

                    map.put(id, entry);
                } else {
                    entry.readyTick = 0;
                    if(charges >= entry.maxCharges) {
                        entry.charges = entry.maxCharges;
                    }
                    map.replace(id, entry);
                }

                Data.API.set(entity, HatredAttachments.ABILITY_COOLDOWNS, map);
            }
        }
    }

    public static void setCooldown(LivingEntity entity, Identifier ability, long cooldown) {
        Identifier cooldownGroup = HatredRegistries.ABILITY.get(ability).cooldownGroup();

        for(Identifier id : HatredRegistries.ABILITY.getIds()) {
            if(HatredRegistries.ABILITY.get(id).cooldownGroup() == cooldownGroup) {
                Map<Identifier, CooldownEntry> map =
                        Data.API.get(entity, HatredAttachments.ABILITY_COOLDOWNS);

                CooldownEntry entry = map.get(id);

                if (entry == null) {
                    entry = new CooldownEntry(
                            entity.getServer().getTicks() + cooldown,
                            HatredRegistries.ABILITY.get(ability).maxCharges(),
                            HatredRegistries.ABILITY.get(ability).maxCharges()
                    );

                    map.put(ability, entry);
                }

                entry.charges--;

                if (entry.charges <= 0) {
                    entry.readyTick = entity.getServer().getTicks() + cooldown;
                }

                Data.API.set(entity, HatredAttachments.ABILITY_COOLDOWNS, map);
            }
        }
    }
}
