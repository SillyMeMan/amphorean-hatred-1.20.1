package net.vinh.hatred.internal.scheduler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.cca.ModComponents;

public final class Schedulers {
    public static WorldScheduler world(ServerWorld world) {
        return ModComponents.WORLD.get(world).getScheduler();
    }

    public static EntityScheduler entity(LivingEntity entity) {
        return ModComponents.LIVING_ENTITY.get(entity).getScheduler();
    }
}
