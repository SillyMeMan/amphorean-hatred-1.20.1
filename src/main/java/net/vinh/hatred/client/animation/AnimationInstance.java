package net.vinh.hatred.client.animation;

public final class AnimationInstance {

    private final AnimationDefinition definition;

    private int elapsedTicks;
    private boolean finished;

    public AnimationInstance(AnimationDefinition definition) {
        this.definition = java.util.Objects.requireNonNull(
                definition,
                "Animation definition cannot be null"
        );
    }

    public void tick() {
        if (finished) {
            return;
        }

        elapsedTicks++;

        if (elapsedTicks >= definition.duration()) {
            elapsedTicks = definition.duration();
            finished = true;
        }
    }

    public float progress(float tickDelta) {
        return Math.min(
                1.0f,
                (elapsedTicks + tickDelta) / definition.duration()
        );
    }

    public BodyTransform sample(String bone, float tickDelta) {
        KeyframeTrack track = definition.getTrack(bone);

        if (track == null) {
            return null;
        }

        return track.sample(progress(tickDelta));
    }

    public boolean isFinished() {
        return finished;
    }

    public int elapsedTicks() {
        return elapsedTicks;
    }

    public AnimationDefinition definition() {
        return definition;
    }
}
