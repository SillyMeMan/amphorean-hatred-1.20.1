package net.vinh.hatred.mixin;

import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.vinh.hatred.internal.ability.state.CombatStates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin {
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void hatred$blockEnderPearlTeleport(HitResult hitResult, CallbackInfo ci) {
        EnderPearlEntity self = (EnderPearlEntity)(Object) this;

        if(self.getOwner() instanceof ServerPlayerEntity serverPlayer && serverPlayer.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            self.discard();
            ci.cancel();
        }
    }
}
