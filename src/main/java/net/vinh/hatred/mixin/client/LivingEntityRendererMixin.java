package net.vinh.hatred.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.vinh.hatred.client.animation.AnimationController;
import net.vinh.hatred.client.animation.AnimationManager;
import net.vinh.hatred.client.animation.BodyTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity>  {
    @Unique
    private boolean hatred$pushed = false;

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void hatred$preRender(T entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        hatred$pushed = false;

        if (entity.getWorld() == null || !entity.getWorld().isClient)
            return;

        AnimationController controller = AnimationManager.get(entity);
        if (controller == null)
            return;

        BodyTransform transform = controller.computeRoot();
        if (transform.equals(BodyTransform.identity()))
            return;

        matrices.push();
        hatred$pushed = true;

        double halfHeight = entity.getHeight() / 2.0;
        matrices.translate(0, halfHeight, 0);

        matrices.translate(
                transform.x(),
                transform.y(),
                transform.z()
        );

        matrices.multiply(
                RotationAxis.POSITIVE_Y.rotation(transform.yaw()));
        matrices.multiply(
                RotationAxis.POSITIVE_X.rotation(transform.pitch()));
        matrices.multiply(
                RotationAxis.POSITIVE_Z.rotation(transform.roll()));
        
        matrices.scale(
                transform.scale(),
                transform.scale(),
                transform.scale()
        );

        matrices.translate(0, -halfHeight, 0);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    private void hatred$postRender(T entity, float entityYaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(!entity.getWorld().isClient) return;
        if(hatred$pushed) {
            matrices.pop();
        }
    }
}
