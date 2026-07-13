package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.vinh.hatred.api.damage.*;
import net.vinh.hatred.internal.scheduler.EntityScheduler;
import net.vinh.hatred.internal.scheduler.ScheduledTask;

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
        return DamageApi.damage(target, totalDamage, distributor, ctx);
    }

    default boolean damage(float damage, DamageContext ctx) {
        return damage(damage, DamageDistributors.FULL_DAMAGE, ctx);
    }
}
