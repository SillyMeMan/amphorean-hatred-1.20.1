package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.ability.AbilityResult;
import net.vinh.hatred.api.ability.Cooldowns;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.api.util.Args;
import net.vinh.hatred.cca.ModComponents;
import net.vinh.hatred.internal.ability.AbilityExecution;
import net.vinh.hatred.internal.ability.AbilityManager;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.damage.DamageDistributors;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.scheduler.EntityScheduler;
import net.vinh.hatred.internal.scheduler.ScheduledTask;

public interface LivingEntityInjectionAccess {
    /**
     * A more configurable version of the {@link Entity#kill()} method.
     * @param builder Primarily used to specify a damage type and/or a custom death message. If the damage type is not specified, it will default to using {@link DamageTypes#GENERIC} and its death message if none is added.
     */
    default void kill(DamageContextBuilder builder) {
        LivingEntity target = (LivingEntity) this;

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

    default void resetCooldown(Ability ability, boolean includeCooldownGroup) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.resetCooldown(entity, HatredRegistries.ABILITY.getId(ability), includeCooldownGroup);
    }

    default void setCooldown(Ability ability, long cooldown) {
        LivingEntity entity = (LivingEntity) this;
        Cooldowns.setCooldown(entity, HatredRegistries.ABILITY.getId(ability), cooldown);
    }

    default void cancelAbility(Ability ability, boolean triggerOnCancelled) {
        LivingEntity entity = (LivingEntity) this;

        Identifier id = HatredRegistries.ABILITY.getId(ability);
        MinecraftServer server = entity.getServer();

        if (server == null || id == null) {
            return;
        }

        AbilityManager manager = AbilityManager.get(server);
        manager.cancel(entity, id, triggerOnCancelled);

        ModComponents.LIVING_ENTITY.get(entity).isUsingAbility = manager.hasActiveExecution(entity);
        ModComponents.LIVING_ENTITY.sync(entity);
    }

    default void cancelAll(boolean triggerOnCancelled) {
        LivingEntity entity = (LivingEntity) this;
        MinecraftServer server = entity.getServer();

        if (server == null) {
            return;
        }

        AbilityManager manager = AbilityManager.get(server);
        manager.cancelAll(entity, triggerOnCancelled);

        ModComponents.LIVING_ENTITY.get(entity).isUsingAbility = false;
        ModComponents.LIVING_ENTITY.sync(entity);
    }

    default AbilityResult attemptAbility(Ability ability) {
        LivingEntity entity = (LivingEntity) this;

        Identifier id = HatredRegistries.ABILITY.getId(ability);

        if (id == null || !Cooldowns.isReady(entity, id)) {
            return AbilityResult.FAIL;
        }

        MinecraftServer server = entity.getServer();

        if (server == null) {
            return AbilityResult.FAIL;
        }

        Args args = ability.preCast(entity).toImmutable();

        AbilityManager manager = AbilityManager.get(server);

        if (manager.hasExecution(entity, id)) {
            return AbilityResult.FAIL;
        }

        AbilityExecution execution = new AbilityExecution(
                entity,
                ability,
                id,
                args
        );

        try {
            execution.initialize();
            manager.start(execution);
        } catch (RuntimeException exception) {
            execution.cancel(false);
            throw exception;
        }

        ModComponents.LIVING_ENTITY.get(entity).isUsingAbility = true;
        ModComponents.LIVING_ENTITY.sync(entity);

        return AbilityResult.SUCCESS;
    }

    default void movementFreeze() {
        LivingEntity self = (LivingEntity) this;

        ModComponents.LIVING_ENTITY.get(self).movementFrozen = true;
        ModComponents.LIVING_ENTITY.sync(self);
    }

    default void movementUnfreeze() {
        LivingEntity self = (LivingEntity) this;

        ModComponents.LIVING_ENTITY.get(self).movementFrozen = false;
        ModComponents.LIVING_ENTITY.sync(self);
    }

    default void lockRotation() {
        LivingEntity self = (LivingEntity) this;

        ModComponents.LIVING_ENTITY.get(self).rotationLocked = true;
        ModComponents.LIVING_ENTITY.sync(self);
    }

    default void unlockRotation() {
        LivingEntity self = (LivingEntity) this;

        ModComponents.LIVING_ENTITY.get(self).rotationLocked = false;
        ModComponents.LIVING_ENTITY.sync(self);
    }

    default void setInvulnerability(boolean isInvulnerable) {
        LivingEntity self = (LivingEntity) this;

        if(isInvulnerable) {
            if(ModComponents.LIVING_ENTITY.get(self).isPureInvulnerable) {
                AmphoreanHatred.LOGGER.warn("{} already has Pure Invulnerability. Remove Pure Invulnerability before applying", self.getEntityName());
                return;
            }

            ModComponents.LIVING_ENTITY.get(self).isInvulnerable = true;
            ModComponents.LIVING_ENTITY.sync(self);
        } else {
            ModComponents.LIVING_ENTITY.get(self).isInvulnerable = false;
            ModComponents.LIVING_ENTITY.sync(self);
        }
    }

    default void setPureInvulnerability(boolean isPureInvulnerable) {
        LivingEntity self = (LivingEntity) this;

        if(isPureInvulnerable) {
            ModComponents.LIVING_ENTITY.get(self).isInvulnerable = false;
            ModComponents.LIVING_ENTITY.get(self).isPureInvulnerable = true;
            ModComponents.LIVING_ENTITY.sync(self);
        } else {
            ModComponents.LIVING_ENTITY.get(self).isPureInvulnerable = false;
            ModComponents.LIVING_ENTITY.sync(self);
        }
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

    default ScheduledTask schedule(long delay, Runnable action) {
        LivingEntity self = (LivingEntity) this;
        return EntityScheduler.schedule(self, delay, action);
    }

    default ScheduledTask scheduleRepeating(long interval, Runnable action) {
        LivingEntity self = (LivingEntity) this;
        return EntityScheduler.scheduleRepeating(self, interval, action);
    }
}
