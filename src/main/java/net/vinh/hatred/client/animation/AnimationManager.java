package net.vinh.hatred.client.animation;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AnimationManager {

    private static final Map<UUID, AnimationController> CONTROLLERS = new HashMap<>();

    public static AnimationController get(LivingEntity entity) {
        return CONTROLLERS.computeIfAbsent(
                entity.getUuid(),
                id -> new AnimationController());
    }

    public static void tick() {
        CONTROLLERS.values().forEach(AnimationController::tick);
    }
}
