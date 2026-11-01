package net.vinh.hatred.client.animation;

public record BodyTransform(
        float pitch,
        float yaw,
        float roll,
        double x,
        double y,
        double z,
        float scale
) {
    public static final BodyTransform IDENTITY = new BodyTransform(
            0.0f, 0.0f, 0.0f,
            0.0, 0.0, 0.0,
            1.0f
    );

    /**
     * Linearly interpolates between two transforms.
     *
     * @param from starting transform
     * @param to target transform
     * @param delta interpolation progress, normally between 0 and 1
     */
    public static BodyTransform interpolate(
            BodyTransform from,
            BodyTransform to,
            float delta
    ) {
        return new BodyTransform(
                lerp(from.pitch, to.pitch, delta),
                lerp(from.yaw, to.yaw, delta),
                lerp(from.roll, to.roll, delta),
                lerp(from.x, to.x, delta),
                lerp(from.y, to.y, delta),
                lerp(from.z, to.z, delta),
                lerp(from.scale, to.scale, delta)
        );
    }

    /**
     * Adds another transform to this transform.
     * Scale is composed multiplicatively.
     */
    public BodyTransform add(BodyTransform other) {
        return new BodyTransform(
                pitch + other.pitch,
                yaw + other.yaw,
                roll + other.roll,
                x + other.x,
                y + other.y,
                z + other.z,
                scale * other.scale
        );
    }

    /**
     * Returns this transform with its translation multiplied by a factor.
     */
    public BodyTransform multiplyTranslation(double factor) {
        return new BodyTransform(
                pitch, yaw, roll,
                x * factor,
                y * factor,
                z * factor,
                scale
        );
    }

    private static float lerp(float from, float to, float delta) {
        return from + (to - from) * delta;
    }

    private static double lerp(double from, double to, float delta) {
        return from + (to - from) * delta;
    }
}
