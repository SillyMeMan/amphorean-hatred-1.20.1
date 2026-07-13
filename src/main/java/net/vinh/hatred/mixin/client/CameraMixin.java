package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.vinh.hatred.client.camera.ScreenshakeController;
import net.vinh.hatred.mixin.accessor.CameraAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Unique
    private static final PerlinNoiseSampler sampler = new PerlinNoiseSampler(Random.createLocal());

    @Inject(method = "update", at = @At("RETURN"))
    private void hatred$shake(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        Camera self = (Camera)(Object) this;
        float strength = ScreenshakeController.getShakeStrength();

        if (strength <= 0f) return;

        float yawOffset = randomizeOffset(strength, 10);
        float pitchOffset = randomizeOffset(strength, -10);
        ((CameraAccessor) self).hatred$invokeSetRotation(self.getYaw() + yawOffset, self.getPitch() + pitchOffset);
    }

    @Unique
    private static float randomizeOffset(float strength, int offset) {
        float min = -strength * 2;
        float max = strength * 2;
        assert MinecraftClient.getInstance().world != null;
        float sampled = (float) sampler.sample((MinecraftClient.getInstance().world.getTime() % 24000L + MinecraftClient.getInstance().getTickDelta())/strength, offset, 0) * 1.5f;
        return min >= max ? min : sampled * max;
    }
}
