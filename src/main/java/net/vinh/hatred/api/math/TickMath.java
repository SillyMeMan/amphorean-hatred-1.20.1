package net.vinh.hatred.api.math;

public final class TickMath {
    private final int currentTicks;

    public TickMath() {
        this(0);
    }

    private TickMath(int newTicks) {
        this.currentTicks = newTicks;
    }

    public TickMath milliseconds(int milliseconds) {
        return new TickMath(currentTicks + milliseconds * 20 / 1000);
    }

    public TickMath seconds(int seconds) {
        return new TickMath(currentTicks + seconds * 20);
    }

    public TickMath minutes(int minutes) {
        return new TickMath(currentTicks + minutes * 20 * 60);
    }

    public TickMath hours(int hours) {
        return new TickMath(currentTicks + hours * 20 * 60 * 60);
    }

    public TickMath reset() {
        return new TickMath();
    }

    public int build() {
        return currentTicks;
    }

    // The one static method here
    public static int ticksFromProgress(float progressPercentage, int duration) {
        return (int)(progressPercentage * duration);
    }
}

