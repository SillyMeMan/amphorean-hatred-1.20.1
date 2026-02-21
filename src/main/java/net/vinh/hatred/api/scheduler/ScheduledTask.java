package net.vinh.hatred.api.scheduler;

public final class ScheduledTask {
    long executeAt;
    long interval;
    boolean repeating;
    boolean cancelled;

    Runnable action;

    ScheduledTask(long executeAt, Runnable action) {
        this.executeAt = executeAt;
        this.action = action;
    }

    public void cancel() {
        this.cancelled = true;
    }
}

