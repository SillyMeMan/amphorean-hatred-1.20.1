package net.vinh.hatred.api.math;

public final class HatredMath {
    private HatredMath() {}

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double inverseLerp(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static double map(double value,
                             double inMin, double inMax,
                             double outMin, double outMax) {
        double t = inverseLerp(value, inMin, inMax);
        return lerp(t, outMin, outMax);
    }

    public static double smoothStep(double t) {
        return t * t * (3 - 2 * t);
    }

    public static double smootherStep(double t) {
        return t * t * t * (t * (6 * t - 15) + 10);
    }
}

