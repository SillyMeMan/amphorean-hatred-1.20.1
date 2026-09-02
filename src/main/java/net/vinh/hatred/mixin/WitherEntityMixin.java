package net.vinh.hatred.mixin;

import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.internal.ability.state.CombatStates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin {
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void hatred$freeze(CallbackInfo ci) {
        WitherEntity self = (WitherEntity) (Object) this;

        if(self.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            self.setVelocity(Vec3d.ZERO);
            self.velocityModified = true;
            ci.cancel();
        }
    }
}
