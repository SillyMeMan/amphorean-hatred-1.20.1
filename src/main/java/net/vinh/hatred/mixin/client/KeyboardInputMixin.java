package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.vinh.hatred.api.ability.state.CombatStates;
import net.vinh.hatred.api.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeMovement(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        if (!Data.API.get(client.player, CombatStates.MOVEMENT_FROZEN)) {
            return;
        }

        Input input = (Input)(Object)this;

        input.movementForward = 0.0F;
        input.movementSideways = 0.0F;

        input.pressingForward = false;
        input.pressingBack = false;
        input.pressingLeft = false;
        input.pressingRight = false;

        input.jumping = false;
        input.sneaking = false;

        ci.cancel();
    }
}
