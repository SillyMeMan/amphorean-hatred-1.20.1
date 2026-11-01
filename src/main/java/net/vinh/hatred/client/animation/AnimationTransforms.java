package net.vinh.hatred.client.animation;

import net.minecraft.client.model.ModelPart;

public final class AnimationTransforms {
    private static final float MODEL_SCALE = 16.0f;

    private AnimationTransforms() {}

    public static void apply(ModelPart part, BodyTransform transform) {
        part.pivotX += (float) (transform.x() * MODEL_SCALE);
        part.pivotY += (float) (transform.y() * MODEL_SCALE);
        part.pivotZ += (float) (transform.z() * MODEL_SCALE);

        part.yaw += (float) Math.toRadians(transform.yaw());
        part.pitch += (float) Math.toRadians(transform.pitch());
        part.roll += (float) Math.toRadians(transform.roll());

        part.xScale *= transform.scale();
        part.yScale *= transform.scale();
        part.zScale *= transform.scale();
    }
}
