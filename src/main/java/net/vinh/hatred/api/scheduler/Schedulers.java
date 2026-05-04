package net.vinh.hatred.api.scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredInternalAttachments;

public final class Schedulers {
    public static WorldScheduler world(ServerWorld world) {
        return Data.API.get(world, HatredInternalAttachments.WORLD_SCHEDULER);
    }

    public static EntityScheduler entity(Entity entity) {
        return Data.API.get(entity, HatredInternalAttachments.ENTITY_SCHEDULER);
    }
}
