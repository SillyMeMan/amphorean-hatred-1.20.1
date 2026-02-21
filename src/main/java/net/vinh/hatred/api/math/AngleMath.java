package net.vinh.hatred.api.math;

public final class AngleMath {
    private AngleMath() {}

    public static float wrapDegrees(float degrees) {
        degrees %= 360.0F;
        if (degrees >= 180.0F) degrees -= 360.0F;
        if (degrees < -180.0F) degrees += 360.0F;
        return degrees;
    }

    public static float angleDifference(float a, float b) {
        return wrapDegrees(b - a);
    }

    public static float lerpAngle(float delta, float start, float end) {
        return start + delta * angleDifference(start, end);
    }
}

