package net.vinh.hatred.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.vinh.hatred.client.animation.AnimationController;
import net.vinh.hatred.client.animation.AnimationManager;
import net.vinh.hatred.client.animation.AnimationModelAdapters;
import net.vinh.hatred.client.animation.BodyTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @WrapMethod(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void hatred$applyAnimation(LivingEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Operation<Void> original) {
        if (!entity.getWorld().isClient) {
            original.call(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        }

        AnimationController controller = AnimationManager.getIfPresent(entity);

        if (controller == null) {
            original.call(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        }

        BodyTransform transform = controller.computeRoot(tickDelta);

        if (transform.equals(BodyTransform.IDENTITY)) {
            original.call(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        }

        matrices.push();

        try {
            matrices.translate(
                    0.0,
                    entity.getHeight() * 0.5,
                    0.0
            );

            matrices.translate(
                    transform.x(),
                    transform.y(),
                    transform.z()
            );

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(transform.yaw()));

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(transform.pitch()));

            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(transform.roll()));

            matrices.scale(
                    transform.scale(),
                    transform.scale(),
                    transform.scale()
            );

            matrices.translate(
                    0.0,
                    -entity.getHeight() * 0.5,
                    0.0
            );

            original.call(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        } finally {
            matrices.pop();
        }
    }

    @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void hatred$applyJointAnimations(EntityModel<?> model, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, Operation<Void> original, @Local(argsOnly = true) LivingEntity entity) {
        AnimationController controller = AnimationManager.getIfPresent(entity);

        if (controller != null) {
            for (String joint : controller.getAnimatedJoints()) {
                BodyTransform transform = controller.computeBone(joint, MinecraftClient.getInstance().getTickDelta());
                AnimationModelAdapters.apply(model, joint, transform);
            }
        }

        original.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
