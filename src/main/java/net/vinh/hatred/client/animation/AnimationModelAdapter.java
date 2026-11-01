package net.vinh.hatred.client.animation;

import net.minecraft.client.render.entity.model.EntityModel;

@FunctionalInterface
public interface AnimationModelAdapter<T extends EntityModel<?>> {
    void apply(T model, String joint, BodyTransform transform);
}
