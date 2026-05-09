package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.damage.DamageDistributor;
import net.vinh.hatred.api.damage.DamageDistributors;
import net.vinh.hatred.internal.data.accessor.EntityMixinAccessor;
import net.vinh.hatred.internal.scheduler.EntityScheduler;
import net.vinh.hatred.internal.scheduler.ScheduledTask;
import net.vinh.hatred.mixin.accessor.LivingEntityAccessor;

public interface EntityInjectionAccess {
    default ScheduledTask schedule(long delay, Runnable action) {
        Entity self = (Entity) this;
        return EntityScheduler.schedule(self, delay, action);
    }

    default ScheduledTask scheduleRepeating(long interval, Runnable action) {
        Entity self = (Entity) this;
        return EntityScheduler.scheduleRepeating(self, interval, action);
    }

    default boolean damage(float totalDamage, DamageDistributor distributor, DamageContext ctx) {
        Entity target = (Entity) this;

        RegistryEntry<DamageType> finalType = ctx.type() != null ? ctx.type() : target.getDamageSources().generic().getTypeRegistryEntry();

        ContextAwareDamageSource source = new ContextAwareDamageSource(finalType, ctx);

        if(ctx.trueDamage() && target instanceof LivingEntity living) return applyTrueDamage(living, distributor.distribute(totalDamage), ctx, source);

        boolean damageApplied = target.damage(source, distributor.distribute(totalDamage));

        if(damageApplied) {
            if(ctx.hitEffects() != null && target instanceof LivingEntity living) ctx.hitEffects().forEach(statusEffectInstance -> living.addStatusEffect(statusEffectInstance, ctx.attacker()));

            if(ctx.knockback() != null) {
                target.setVelocity(ctx.knockback());
            } else {
                target.setVelocity(Vec3d.ZERO);
            }

            target.velocityModified = true;
        }

        return damageApplied;
    }

    default boolean damage(float damage, DamageContext ctx) {
        return damage(damage, DamageDistributors.FULL_DAMAGE, ctx);
    }

    private boolean applyTrueDamage(LivingEntity target, float damage, DamageContext ctx, ContextAwareDamageSource source) {
        if (target.isInvulnerableTo(source) || (target instanceof PlayerEntity player && player.getAbilities().invulnerable && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))) {
            return false;
        } else if (target.getWorld().isClient) {
            return false;
        } else if (target.isDead()) {
            return false;
        } else if (source.isIn(DamageTypeTags.IS_FIRE) && target.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            float health = target.getHealth();
            float newHealth = ctx.nonFatal() ? Math.max(1f, health - damage) : Math.max(0f, health - damage);
            RegistryEntry<DamageType> finalType = ctx.type() != null ? ctx.type() : target.getDamageSources().generic().getTypeRegistryEntry();

            target.getDamageTracker().onDamage(
                    new ContextAwareDamageSource(finalType, ctx),
                    damage
            );

            target.setHealth(newHealth);

            ((LivingEntityAccessor) target).hatred$invokePlayHurtSound(new ContextAwareDamageSource(finalType, ctx));

            target.timeUntilRegen = 20;
            target.hurtTime = 10;
            target.maxHurtTime = 10;

            if (newHealth <= 0) {
                target.onDeath(new ContextAwareDamageSource(finalType, ctx));
            }

            return true;
        }
    }
}
