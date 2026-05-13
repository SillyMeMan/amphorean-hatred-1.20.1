package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.vinh.hatred.client.camera.ScreenshakeController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private float yaw;
    @Shadow private float pitch;

    @Inject(method = "update", at = @At("TAIL"))
    private void hatred$shake(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        float strength = ScreenshakeController.getShakeStrength();

        if (strength <= 0f) return;

        MinecraftClient client = MinecraftClient.getInstance();

        float time = (client.world.getTime() + tickDelta) * 0.6f;

        this.yaw += MathHelper.sin(time * 3.0f) * strength;
        this.pitch += MathHelper.cos(time * 2.5f) * strength * 0.7f;
    }
}
