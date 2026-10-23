package net.vinh.hatred.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.vinh.hatred.mixin.accessor.LivingEntityAccessor;

/**
 * The main implementation of the damage system. Injected into {@link Entity} via Loom's Interface Injection. Any mixins should target the methods in this class.
 */
public final class DamageApi {
    private DamageApi() {}

    public static boolean damage(Entity target, float totalDamage, DamageDistributor distributor, DamageContext ctx) {
        RegistryEntry<DamageType> finalType = ctx.type() != null ? ctx.type() : target.getDamageSources().generic().getTypeRegistryEntry();

        ContextAwareDamageSource source = new ContextAwareDamageSource(finalType, ctx);

        float distributedDamage = distributor.distribute(totalDamage);

        boolean damageApplied = ctx.trueDamage() && target instanceof LivingEntity living ? applyTrueDamage(living, distributedDamage, ctx, source) : target.damage(source, distributedDamage);

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

    private static boolean applyTrueDamage(
            LivingEntity target,
            float damage,
            DamageContext ctx,
            ContextAwareDamageSource source
    ) {
        if (target.getWorld().isClient || target.isDead() || damage <= 0.0F) {
            return false;
        }

        if (target.isInvulnerableTo(source)) {
            return false;
        }

        if (source.isIn(DamageTypeTags.IS_FIRE) && target.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
            return false;
        }

        if (target.isSleeping()) {
            target.wakeUp();
        }

        target.setDespawnCounter(0);

        LivingEntityAccessor accessor = (LivingEntityAccessor) target;

        boolean inCooldown = !ctx.bypassesCooldown()
                && target.timeUntilRegen > 10;

        float effectiveDamage = damage;

        if (inCooldown) {
            float lastDamage = accessor.hatred$accessLastDamageTaken();

            if (damage <= lastDamage) {
                return false;
            }

            effectiveDamage = damage - lastDamage;
        } else {
            target.timeUntilRegen = 20;
            target.maxHurtTime = 10;
            target.hurtTime = target.maxHurtTime;
        }

        accessor.hatred$setLastDamageTaken(damage);

        float healthDamage = effectiveDamage;

        if (!ctx.bypassesAbsorption()) {
            float absorption = target.getAbsorptionAmount();
            float absorbedDamage = Math.min(absorption, healthDamage);

            target.setAbsorptionAmount(absorption - absorbedDamage);
            healthDamage -= absorbedDamage;
        }

        float oldHealth = target.getHealth();
        float newHealth = ctx.nonFatal()
                ? Math.max(1.0F, oldHealth - healthDamage)
                : Math.max(0.0F, oldHealth - healthDamage);

        if (healthDamage > 0.0F) {
            target.getDamageTracker().onDamage(source, healthDamage);
            target.setHealth(newHealth);
            target.emitGameEvent(GameEvent.ENTITY_DAMAGE);
        }

        accessor.hatred$invokePlayHurtSound(source);

        if (newHealth <= 0.0F && oldHealth > 0.0F) {
            if(!accessor.hatred$invokeTryUseTotem(source)) {
                target.onDeath(source);
            }
        }

        return true;
    }
}
