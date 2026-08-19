package net.vinh.hatred.internal.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.ability.AbilityResult;
import net.vinh.hatred.api.ability.Cooldowns;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.api.misc.Args;
import net.vinh.hatred.internal.ability.state.CombatStates;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.damage.DamageDistributors;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.event.ServerAbilityEvents;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.HatredInternalAttachments;
import net.vinh.hatred.internal.ability.AbstractAbility;
import net.vinh.hatred.util.Utils;

import java.util.Map;
import java.util.Objects;

public interface LivingEntityInjectionAccess {
    default void kill(RegistryEntry<DamageType> type) {
        LivingEntity target = (LivingEntity) this;

        DamageContextBuilder builder = Utils.Builders.contextBuilder();

        builder.type(type);

        builder.bypassesTotems();
        builder.bypassesAbsorption();
        builder.bypassesShield();
        builder.bypassesResistance();
        builder.bypassesInvulnerability();
        builder.bypassesEnchantments();
        builder.bypassesCooldown();
        builder.bypassesArmor();

        builder.alwaysDamageEnderDragons();

        target.damage(Float.MAX_VALUE, builder.build());
    }

    default boolean damage(double percentage, DamageContext ctx) {
        LivingEntity target = (LivingEntity) this;

        if(percentage < 0 || percentage > 1) throw new IllegalArgumentException("Percentage must be smaller or equal to 1 and non-negative");

        return target.damage((float) ((target.getMaxHealth() + target.getAbsorptionAmount()) * percentage), DamageDistributors.FULL_DAMAGE, ctx);
    }

    default void resetAllCooldowns() {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.resetAllCooldown(entity);
    }

    default void resetCooldown(Ability ability) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.resetCooldown(entity, HatredRegistries.ABILITY.getId(ability));
    }

    default void setCooldown(Ability ability, long cooldown) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.setCooldown(entity, HatredRegistries.ABILITY.getId(ability), cooldown);
    }

    default void cancelAbility(Ability ability, boolean triggerOnCancelled) {
        LivingEntity entity = (LivingEntity) this;

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        for (AbstractAbility.PreCastInstance instance : map.values()) {
            if(instance.abilityId == HatredRegistries.ABILITY.getId(ability)) {
                instance.cancelled = true;
                if(triggerOnCancelled) ability.onCancelled(entity);

                Data.API.set(entity, HatredInternalAttachments.IS_USING_ABILITY, false);
            }
        }

        map.clear();
    }

    default void cancelAll(boolean triggerOnCancelled) {
        LivingEntity entity = (LivingEntity) this;

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        for(AbstractAbility.PreCastInstance instance : map.values()) {
            instance.cancelled = true;
            if(triggerOnCancelled) {
                Objects.requireNonNull(HatredRegistries.ABILITY.get(instance.abilityId)).onCancelled(entity);
            }
        }

        Data.API.set(entity, HatredInternalAttachments.IS_USING_ABILITY, false);
    }

    default AbilityResult attemptAbility(Ability ability) {
        var ref = new Object() {
            AbilityResult finalResult = AbilityResult.PASS;
        };

        LivingEntity entity = (LivingEntity) this;

        Identifier id = HatredRegistries.ABILITY.getId(ability);

        Map<Identifier, AbstractAbility.PreCastInstance> map =
                Data.API.get(entity, HatredInternalAttachments.PRECASTS);

        if (!Cooldowns.isReady(entity, id) || ServerAbilityEvents.PRE_CAST.invoker().preCast(entity, ability) == AbilityResult.FAIL) {
            ref.finalResult = AbilityResult.FAIL;
            return ref.finalResult;
        }

        Args args = ability.preCast(entity);
        Data.API.set(entity, HatredInternalAttachments.IS_USING_ABILITY, true);

        assert entity.getServer() != null;
        long now = entity.getServer().getTicks();

        long castTick = now + ability.preCastTime();

        AbstractAbility.PreCastInstance instance =
                new AbstractAbility.PreCastInstance(id, now, castTick);

        map.put(id, instance);

        Data.API.set(entity, HatredInternalAttachments.PRECASTS, map);

        entity.schedule(ability.preCastTime(), () -> {
            if (!instance.cancelled || ServerAbilityEvents.PRE_CAST.invoker().preCast(entity, ability) != AbilityResult.CANCELLED) {
                ability.cast(entity, args.toImmutable());
                ref.finalResult = AbilityResult.SUCCESS;
                Cooldowns.setCooldown(entity, id, ability.cooldown());

                Map<Identifier, AbstractAbility.PreCastInstance> precasts =
                        Data.API.get(entity, HatredInternalAttachments.PRECASTS);

                precasts.remove(id);

                Data.API.set(entity, HatredInternalAttachments.PRECASTS, precasts);
            } else {
                ref.finalResult = AbilityResult.CANCELLED;
                Cooldowns.setCooldown(entity, id, ability.interruptCooldown());

                Map<Identifier, AbstractAbility.PreCastInstance> precasts =
                        Data.API.get(entity, HatredInternalAttachments.PRECASTS);

                precasts.remove(id);

                Data.API.set(entity, HatredInternalAttachments.PRECASTS, precasts);
            }

            Data.API.set(entity, HatredInternalAttachments.IS_USING_ABILITY, false);
        });

        return ref.finalResult;
    }

    default void movementFreeze() {
        LivingEntity self = (LivingEntity) this;

        StatusEffectInstance instance = new StatusEffectInstance(CombatStates.MOVEMENT_FREEZE, -1, 0, false, false);

        self.addStatusEffect(instance);
    }

    default void movementUnfreeze() {
        LivingEntity self = (LivingEntity) this;

        self.removeStatusEffect(CombatStates.MOVEMENT_FREEZE);
    }

    default void lockRotation() {
        LivingEntity self = (LivingEntity) this;

        StatusEffectInstance instance = new StatusEffectInstance(CombatStates.ROTATION_LOCK, -1, 0, false, false);

        self.addStatusEffect(instance);
    }

    default void unlockRotation() {
        LivingEntity self = (LivingEntity) this;

        self.removeStatusEffect(CombatStates.ROTATION_LOCK);
    }

    default void completeFreeze() {
        LivingEntity self = (LivingEntity) this;

        self.movementFreeze();
        self.lockRotation();

        if(self instanceof PlayerEntity player) player.inventoryFreeze();

    }

    default void completeUnfreeze() {
        LivingEntity self = (LivingEntity) this;

        self.movementUnfreeze();
        self.unlockRotation();

        if(self instanceof PlayerEntity player) player.inventoryUnfreeze();
    }
}
