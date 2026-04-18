package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.ability.AbilityResult;
import net.vinh.hatred.api.ability.Cooldowns;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.damage.DamageDistributors;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.event.ServerAbilityEvents;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.api.scheduler.EntityScheduler;
import net.vinh.hatred.internal.HatredAttachments;
import net.vinh.hatred.internal.ability.AbstractAbility;

import java.util.Map;

public interface LivingEntityInjectionAccess {
    default boolean damage(double percentage, DamageContext ctx) {
        LivingEntity target = (LivingEntity) this;

        if(percentage < 0 || percentage > 1) throw new IllegalArgumentException("Percentage must be smaller or equal to 1 and non-negative");

        return target.damage((float) (target.getMaxHealth() * percentage), DamageDistributors.FULL_DAMAGE, ctx);
    }

    default void resetAllCooldowns() {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.resetAllCooldown(entity);
    }

    default void resetCooldown(Ability ability) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.resetCooldown(entity, HatredRegistries.ABILITY.getId(ability));
    }

    default void setCharges(Ability ability, int charges) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.setCharges(entity, HatredRegistries.ABILITY.getId(ability), charges);
    }

    default void setCooldown(Ability ability, long cooldown) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.setCooldown(entity, HatredRegistries.ABILITY.getId(ability), cooldown);
    }

    default void cancelAbility(Ability ability) {
        LivingEntity entity = (LivingEntity) this;

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        for (AbstractAbility.PreCastInstance instance : map.values()) {
            if(instance.abilityId == HatredRegistries.ABILITY.getId(ability)) {
                instance.cancelled = true;
            }
        }

        map.clear();
    }

    default void cancelAll() {
        LivingEntity entity = (LivingEntity) this;

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        for(AbstractAbility.PreCastInstance instance : map.values()) {
            instance.cancelled = true;
        }
    }

    default AbilityResult attemptAbility(Ability ability) {
        var ref = new Object() {
            AbilityResult finalResult = AbilityResult.PASS;
        };

        LivingEntity entity = (LivingEntity) this;

        Identifier id = HatredRegistries.ABILITY.getId(ability);

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredAttachments.PRECASTS);

        if (!Cooldowns.isReady(entity, id) || ServerAbilityEvents.PRE_CAST.invoker().preCast(entity, ability) == AbilityResult.FAIL) {
            ref.finalResult = AbilityResult.FAIL;
            return ref.finalResult;
        }

        ability.preCast(entity);

        assert entity.getServer() != null;
        long now = entity.getServer().getTicks();

        long castTick = now + ability.preCastTime();

        AbstractAbility.PreCastInstance instance =
                new AbstractAbility.PreCastInstance(id, now, castTick);

        map.put(id, instance);

        Data.API.set(entity, HatredAttachments.PRECASTS, map);

        EntityScheduler.schedule(entity, ability.preCastTime(), () -> {
            if (!instance.cancelled || ServerAbilityEvents.PRE_CAST.invoker().preCast(entity, ability) != AbilityResult.CANCELLED) {
                ability.cast(entity);
                ref.finalResult = AbilityResult.SUCCESS;
                Cooldowns.setCooldown(entity, id, ability.cooldown());

                Map<Identifier, AbstractAbility.PreCastInstance> precasts =
                        Data.API.get(entity, HatredAttachments.PRECASTS);

                precasts.remove(id);

                Data.API.set(entity, HatredAttachments.PRECASTS, precasts);
            } else {
                ref.finalResult = AbilityResult.CANCELLED;
                Cooldowns.setCooldown(entity, id, ability.interruptCooldown());

                Map<Identifier, AbstractAbility.PreCastInstance> precasts =
                        Data.API.get(entity, HatredAttachments.PRECASTS);

                precasts.remove(id);

                Data.API.set(entity, HatredAttachments.PRECASTS, precasts);
            }
        });

        return ref.finalResult;
    }
}
