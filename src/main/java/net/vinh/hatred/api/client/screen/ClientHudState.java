package net.vinh.hatred.api.client.screen;

public class ClientHudState {
    private static HudTextEntry current;

    public static void set(HudTextEntry entry) {
        current = entry;
    }

    public static void clear() {
        current = null;
    }

    public static HudTextEntry get() {
        return current;
    }

    public static void tick() {
        if (current != null) {
            current.tick();
            if (current.isExpired()) {
                current = null;
            }
        }
    }
}
