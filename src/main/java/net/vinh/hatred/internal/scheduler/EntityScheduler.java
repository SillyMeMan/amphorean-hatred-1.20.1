package net.vinh.hatred.internal.scheduler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.cca.ModComponents;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    public static ScheduledTask schedule(LivingEntity entity, long delay, Runnable action) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return null;

        EntityScheduler scheduler = ModComponents.LIVING_ENTITY.get(entity).getScheduler();

        WorldScheduler worldScheduler = ModComponents.WORLD.get(world).getScheduler();

        if (scheduler.isEmpty()) {
            worldScheduler.registerEntityScheduler(scheduler);
        }

        return scheduler.scheduleInternal(delay, action, worldScheduler.getInternalTick());
    }

    @Nullable
    public static ScheduledTask scheduleRepeating(Entity entity, long interval, Runnable action) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return null;

        EntityScheduler scheduler = ModComponents.LIVING_ENTITY.get(entity).getScheduler();

        WorldScheduler worldScheduler = ModComponents.WORLD.get(world).getScheduler();

        if (scheduler.isEmpty()) {
            worldScheduler.registerEntityScheduler(scheduler);
        }

        return scheduler.scheduleRepeatingInternal(interval, action, worldScheduler.getInternalTick());
    }

    public ScheduledTask scheduleInternal(long delay, Runnable action, long worldTick) {
        ScheduledTask task = new ScheduledTask(worldTick + delay, action);
        queue.add(task);
        return task;
    }

    public ScheduledTask scheduleRepeatingInternal(long interval, Runnable action, long worldTick) {
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

