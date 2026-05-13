package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.client.camera.ScreenshakeController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void hatred$applyCameraShake(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        float strength = ScreenshakeController.getShakeStrength();
        if (strength <= 0f) return;

        float time = (client.world.getTime() + tickDelta) * 0.6f;

        float yawShake   = MathHelper.sin(time * 3.0f) * strength;
        float pitchShake = MathHelper.cos(time * 2.5f) * strength * 0.7f;

        AmphoreanHatred.LOGGER.info("If this doesn't print, then mixin got broken somehow");

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawShake));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchShake));
    }
}
