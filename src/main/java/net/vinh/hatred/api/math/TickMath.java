package net.vinh.hatred.api.math;

public final class TickMath {
    private TickMath() {}

    public static int milliseconds(int milliseconds) {
        return milliseconds * 20 / 1000;
    }

    public static int seconds(int seconds) {
        return seconds * 20;
    }

    public static int minutes(int minutes) {
        return minutes * 20 * 60;
    }

    public static int hours(int hours) {
        return hours * 20 * 60^2;
    }

    public static int ticksFromProgress(float progress, int duration) {
        return (int)(progress * duration);
    }
}

