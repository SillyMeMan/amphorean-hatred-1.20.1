package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.vinh.hatred.client.camera.ScreenshakeController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique
    private static final PerlinNoiseSampler SHAKE_NOISE = new PerlinNoiseSampler(Random.create(1337L));

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void minuet$applyCameraShake(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float strength = ScreenshakeController.getShakeStrength();
        if (strength <= 0f) return;

        float time = (client.world.getTime() + tickDelta) * 0.05f;

        double yawNoise   = SHAKE_NOISE.sample(time, 0, 0);
        double pitchNoise = SHAKE_NOISE.sample(time, 100, 0);
        double rollNoise  = SHAKE_NOISE.sample(time, 200, 0);

        float yawShake   = (float) yawNoise   * strength * 2.0f;
        float pitchShake = (float) pitchNoise * strength * 1.5f;
        float rollShake  = (float) rollNoise  * strength * 0.7f;

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawShake));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchShake));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rollShake));
    }
}
