package net.vinh.hatred.api.scheduler;

import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ScheduledTimeline {
    private final List<ScheduledKeyframe> frames;
    private final ServerWorld world;

    public ScheduledTimeline(ServerWorld world) {
        this.world = world;
        this.frames = new ArrayList<>();
    }

    public ScheduledTimeline addFrames(ScheduledKeyframe... keyframes) {
        this.frames.addAll(Arrays.asList(keyframes));
        return this;
    }

    public ScheduledTimeline scheduleFramesInOrder() {
        reorder();
        this.frames.forEach(keyframe -> world.schedule(keyframe.delayFromStart(), keyframe.scheduledAction()));
        return this;
    }

    private void reorder() {
        this.frames.sort(Comparator.comparingLong(ScheduledKeyframe::delayFromStart));
    }
}
