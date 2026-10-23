package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.vinh.hatred.api.damage.*;

public interface EntityInjectionAccess {
    default boolean damage(float totalDamage, DamageDistributor distributor, DamageContext ctx) {
        Entity target = (Entity) this;
        return DamageApi.damage(target, totalDamage, distributor, ctx);
    }

    default boolean damage(float damage, DamageContext ctx) {
        return damage(damage, DamageDistributors.FULL_DAMAGE, ctx);
    }
}
