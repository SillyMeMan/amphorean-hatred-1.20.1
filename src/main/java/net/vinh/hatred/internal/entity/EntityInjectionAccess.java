package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.api.damage.DamageContext;

public interface EntityInjectionAccess {
    default boolean damage(DamageContext ctx) {
        Entity target = (Entity) this;

        RegistryEntry<DamageType> finalType = ctx.type() != null ? ctx.type() : target.getDamageSources().generic().getTypeRegistryEntry();

        if(ctx.trueDamage() && target instanceof LivingEntity living) return applyTrueDamage(living, ctx);

        ContextAwareDamageSource source = new ContextAwareDamageSource(finalType, ctx);

        boolean damageApplied = target.damage(source, ctx.rawDamage());

        if(damageApplied) {
            if(ctx.hitEffects() != null && target instanceof LivingEntity living) ctx.hitEffects().forEach(statusEffectInstance -> living.addStatusEffect(statusEffectInstance, ctx.attacker()));

            if(ctx.knockback() != null) {
                target.setVelocity(ctx.knockback());
                target.velocityModified = true;
            }
        }

        return damageApplied;
    }

    private boolean applyTrueDamage(LivingEntity target, DamageContext ctx) {
        float health = target.getHealth();
        float newHealth = Math.max(0.0F, health - ctx.rawDamage());
        RegistryEntry<DamageType> finalType = ctx.type() != null ? ctx.type() : target.getDamageSources().generic().getTypeRegistryEntry();

        target.getDamageTracker().onDamage(
                new ContextAwareDamageSource(finalType, ctx),
                ctx.rawDamage()
        );

        target.setHealth(newHealth);

        target.playHurtSound(new ContextAwareDamageSource(finalType, ctx));

        target.timeUntilRegen = 20;
        target.hurtTime = 10;
        target.maxHurtTime = 10;

        if (newHealth <= 0) {
            target.onDeath(new ContextAwareDamageSource(finalType, ctx));
        }

        return true;
    }
}
