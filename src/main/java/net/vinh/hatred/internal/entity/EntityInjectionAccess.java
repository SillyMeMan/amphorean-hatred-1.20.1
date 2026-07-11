package net.vinh.hatred.internal.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.damage.*;
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
        return DamageApi.damage(target, totalDamage, distributor, ctx);
    }

    default boolean damage(float damage, DamageContext ctx) {
        return damage(damage, DamageDistributors.FULL_DAMAGE, ctx);
    }
}
