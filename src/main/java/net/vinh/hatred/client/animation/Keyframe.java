package net.vinh.hatred.client.animation;


public record Keyframe(
        float time,
        BodyTransform transform
) {
    public Keyframe {
        if (!Float.isFinite(time) || time < 0.0f || time > 1.0f) {
            throw new IllegalArgumentException(
                    "Keyframe time must be between 0.0 and 1.0: " + time
            );
        }

        if (transform == null) {
            throw new IllegalArgumentException(
                    "Keyframe transform cannot be null"
            );
        }
    }
}

