package net.vinh.hatred.api.math;

public final class TickMath {
    private int currentTicks;

    public TickMath() {
        this.currentTicks = 0;
    }

    public TickMath milliseconds(int milliseconds) {
        this.currentTicks += milliseconds / 50;
        return this;
    }

    public TickMath seconds(int seconds) {
        this.currentTicks += seconds * 20;
        return this;
    }

    public TickMath minutes(int minutes) {
        this.currentTicks += minutes * 20 * 60;
        return this;
    }

    public TickMath hours(int hours) {
        this.currentTicks += hours * 20 * 60 * 60;
        return this;
    }

    public TickMath reset() {
        this.currentTicks = 0;
        return this;
    }

    public long build() {
        return currentTicks;
    }

    // The one static method here
    public static int ticksFromProgress(float progressPercentage, int duration) {
        return (int)(progressPercentage * duration);
    }
}

