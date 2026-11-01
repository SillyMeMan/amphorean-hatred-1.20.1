package net.vinh.hatred.client.animation;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AnimationManager {
    private static final Map<UUID, AnimationController> CONTROLLERS =
            new HashMap<>();

    private AnimationManager() {}

    public static AnimationController get(LivingEntity entity) {
        return CONTROLLERS.computeIfAbsent(
                entity.getUuid(),
                uuid -> new AnimationController()
        );
    }

    public static void tick() {
        CONTROLLERS.values().removeIf(controller -> {
            controller.tick();
            return !controller.isPlaying();
        });
    }

    public static void remove(LivingEntity entity) {
        CONTROLLERS.remove(entity.getUuid());
    }

    public static void clear() {
        CONTROLLERS.clear();
    }

    public static int controllerCount() {
        return CONTROLLERS.size();
    }

    public static AnimationController getIfPresent(LivingEntity entity) {
        return CONTROLLERS.get(entity.getUuid());
    }
}
