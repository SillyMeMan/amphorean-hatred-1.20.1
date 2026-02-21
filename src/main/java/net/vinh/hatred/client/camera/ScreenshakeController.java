package net.vinh.hatred.client.camera;

public class ScreenshakeController {
    private static float intensity = 0f;
    private static int ticksLeft = 0;
    private static int totalTicks = 0;

    public static void shake(float strength, int durationTicks) {
        intensity = Math.max(intensity, strength);
        ticksLeft = Math.max(ticksLeft, durationTicks);
        totalTicks = ticksLeft;
    }

    public static float getShakeStrength() {
        if (ticksLeft <= 0) return 0f;

        float progress = ticksLeft / (float) totalTicks;
        return intensity * progress;
    }

    public static void tick() {
        if (ticksLeft > 0) {
            ticksLeft--;
            if (ticksLeft == 0) intensity = 0f;
        }
    }
}
