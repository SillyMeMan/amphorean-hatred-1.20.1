package net.vinh.hatred.api.scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.api.data.Data;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import static net.vinh.hatred.internal.HatredAttachments.*;

public final class EntityScheduler {

    private final PriorityQueue<ScheduledTask> queue =
            new PriorityQueue<>(Comparator.comparingLong(t -> t.executeAt));

    public void tick(long tick) {
        while (!queue.isEmpty() && queue.peek().executeAt <= tick) {
            ScheduledTask task = queue.poll();

            if (task.cancelled) continue;

            task.action.run();

            if (task.repeating && !task.cancelled) {
                task.executeAt = tick + task.interval;
                queue.add(task);
            }
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Nullable
    public static ScheduledTask schedule(Entity entity, long delay, Runnable action) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return null;

        EntityScheduler scheduler =
                Data.API.get(entity, ENTITY_SCHEDULER);

        WorldScheduler worldScheduler =
                Data.API.get(world, WORLD_SCHEDULER);

        if (scheduler.isEmpty()) {
            worldScheduler.registerEntityScheduler(scheduler);
        }

        return scheduler.schedule(delay, action, worldScheduler.getInternalTick());
    }

    @Nullable
    public static ScheduledTask scheduleRepeating(Entity entity, long interval, Runnable action) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return null;

        EntityScheduler scheduler =
                Data.API.get(entity, ENTITY_SCHEDULER);

        WorldScheduler worldScheduler =
                Data.API.get(world, WORLD_SCHEDULER);

        if (scheduler.isEmpty()) {
            worldScheduler.registerEntityScheduler(scheduler);
        }

        return scheduler.scheduleRepeating(interval, action, worldScheduler.getInternalTick());
    }

    public ScheduledTask schedule(long delay, Runnable action, long worldTick) {
        ScheduledTask task = new ScheduledTask(worldTick + delay, action);
        queue.add(task);
        return task;
    }

    public ScheduledTask scheduleRepeating(long interval, Runnable action, long worldTick) {
        ScheduledTask task = new ScheduledTask(worldTick + interval, action);
        task.repeating = true;
        task.interval = interval;
        queue.add(task);
        return task;
    }

    public void clear() {
        queue.clear();
    }
}

