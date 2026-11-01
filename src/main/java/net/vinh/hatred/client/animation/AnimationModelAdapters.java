package net.vinh.hatred.client.animation;

import net.minecraft.client.render.entity.model.EntityModel;

import java.util.HashMap;
import java.util.Map;

public final class AnimationModelAdapters {
    private static final Map<Class<?>, AnimationModelAdapter<?>> ADAPTERS =
            new HashMap<>();

    private AnimationModelAdapters() {}

    public static <T extends EntityModel<?>> void register(
            Class<?> modelClass,
            AnimationModelAdapter<T> adapter
    ) {
        ADAPTERS.put(modelClass, adapter);
    }

    @SuppressWarnings("unchecked")
    public static void apply(EntityModel<?> model, String joint, BodyTransform transform) {
        AnimationModelAdapter<EntityModel<?>> adapter = (AnimationModelAdapter<EntityModel<?>>) ADAPTERS.get(model.getClass());

        if (adapter != null) {
            adapter.apply(model, joint, transform);
        }
    }
}
