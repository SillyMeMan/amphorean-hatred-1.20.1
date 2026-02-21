package net.vinh.hatred.api.damage;

import net.minecraft.entity.LivingEntity;

public final class DamageUtil {
    public static boolean apply(LivingEntity target, DamageContext ctx) {
        ContextAwareDamageSource source = new ContextAwareDamageSource(ctx.source().getTypeRegistryEntry(), ctx);

        boolean damageApplied = target.damage(source, ctx.rawDamage());

        if(damageApplied) {
            if(ctx.hitEffects() != null) ctx.hitEffects().forEach(statusEffectInstance -> target.addStatusEffect(statusEffectInstance, ctx.attacker()));

            if(ctx.knockback() != null) {
                target.setVelocity(ctx.knockback());
                target.velocityModified = true;
            }
        }

        return damageApplied;
    }
}
