package net.vinh.hatred.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void minuet$bypassesTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof ContextAwareDamageSource ctx && ctx.context().bypassTotems()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
    private void minuet$bypassesArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if(source instanceof ContextAwareDamageSource ctx && ctx.context().bypassArmor()) {
            cir.setReturnValue(amount);
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At("HEAD"), cancellable = true)
    private void minuet$bypassesResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if(source instanceof ContextAwareDamageSource ctx && ctx.context().bypassResistance()) {
            cir.setReturnValue(amount);
        }
    }
}
