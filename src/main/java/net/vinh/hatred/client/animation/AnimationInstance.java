package net.vinh.hatred.client.animation;

public class AnimationInstance {
    private final AnimationDefinition definition;
    private int tick;

    public AnimationInstance(AnimationDefinition definition) {
        this.definition = definition;
    }

    public void tick() {
        tick++;
    }

    public boolean isFinished() {
        return tick >= definition.duration();
    }

    public float progress() {
        return Math.min(1f,
                (float) tick / definition.duration());
    }

    public AnimationDefinition getDefinition() {
        return definition;
    }
}
