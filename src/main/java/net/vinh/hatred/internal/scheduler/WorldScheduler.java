package net.vinh.hatred.internal.scheduler;

import net.vinh.hatred.AmphoreanHatred;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

public final class WorldScheduler {

    private final Set<EntityScheduler> entitySchedulers = new HashSet<>();

    private final PriorityQueue<ScheduledTask> queue =
            new PriorityQueue<>(Comparator.comparingLong(t -> t.executeAt));

    private long internalTick = 0L;

    public void tick() {
        internalTick++;

        while (!queue.isEmpty() && queue.peek().executeAt <= internalTick) {

            ScheduledTask task = queue.poll();

            if (task.cancelled) continue;

            try {
                task.action.run();
            } catch (Throwable t) {
                AmphoreanHatred.LOGGER.error("A throwable was caught: {}", t.getMessage());
                t.printStackTrace();
            }

            if (task.repeating && !task.cancelled) {
                task.executeAt = internalTick + task.interval;
                queue.add(task);
            }
        }

        Iterator<EntityScheduler> it = entitySchedulers.iterator();

        while (it.hasNext()) {
            EntityScheduler scheduler = it.next();

            scheduler.tick(internalTick);

            if (scheduler.isEmpty()) {
                it.remove();
            }
        }
    }

    public ScheduledTask schedule(long delay, Runnable action) {
        AmphoreanHatred.LOGGER.info("Scheduling: {}", System.identityHashCode(this));

        ScheduledTask task = new ScheduledTask(internalTick + delay, action);
        queue.add(task);
        return task;
    }

    public ScheduledTask scheduleRepeating(long interval, Runnable action) {
        ScheduledTask task = new ScheduledTask(internalTick + interval, action);
        task.repeating = true;
        task.interval = interval;
        queue.add(task);
        return task;
    }

    public void cancel(ScheduledTask task) {
        task.cancelled = true;
    }

    @ApiStatus.Internal
    public void registerEntityScheduler(EntityScheduler scheduler) {
        entitySchedulers.add(scheduler);
    }

    public long getInternalTick() {
        return internalTick;
    }
}


