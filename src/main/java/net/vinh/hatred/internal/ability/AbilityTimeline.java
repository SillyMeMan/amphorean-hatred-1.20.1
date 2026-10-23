package net.vinh.hatred.internal.ability;

public interface AbilityTimeline {
    void atTick(long tick, Runnable action);
}
