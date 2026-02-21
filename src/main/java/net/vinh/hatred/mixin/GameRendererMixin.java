package net.vinh.hatred.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.vinh.hatred.client.camera.ScreenshakeController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO: Change the screen shake system to use Perlin noise maps
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void minuet$applyCameraShake(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float strength = ScreenshakeController.getShakeStrength();
        if (strength <= 0f) return;

        float time = (client.world.getTime() + tickDelta) * 0.6f;

        float yawShake   = MathHelper.sin(time * 3.0f) * strength;
        float pitchShake = MathHelper.cos(time * 2.5f) * strength * 0.7f;

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawShake));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchShake));
    }
}
