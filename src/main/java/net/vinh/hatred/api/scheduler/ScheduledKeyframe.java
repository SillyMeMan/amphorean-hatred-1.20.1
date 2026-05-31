package net.vinh.hatred.api.scheduler;

public record ScheduledKeyframe(long delayFromStart, Runnable scheduledAction) {}
