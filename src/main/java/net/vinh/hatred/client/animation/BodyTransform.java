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

    public static BodyTransform identity() {
        return new BodyTransform(0,0,0,0,0,0,1f);
    }

    public static BodyTransform interpolate(
            BodyTransform a,
            BodyTransform b,
            float t
    ) {
        return new BodyTransform(
                lerp(a.pitch, b.pitch, t),
                lerp(a.yaw,   b.yaw,   t),
                lerp(a.roll,  b.roll,  t),
                lerp(a.x,     b.x,     t),
                lerp(a.y,     b.y,     t),
                lerp(a.z,     b.z,     t),
                lerp(a.scale, b.scale, t)
        );
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static double lerp(double a, double b, float t) {
        return a + (b - a) * t;
    }
}
