package net.vinh.hatred.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredAttachments;
import net.vinh.hatred.internal.entity.LivingEntityInjectionAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityInjectionAccess {
    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void hatred$bypassesTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesTotems()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
    private void hatred$bypassesArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if(source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesArmor()) {
            cir.setReturnValue(amount);
        }
    }

    @Redirect(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    private boolean hatred$bypassesResistance(LivingEntity self, StatusEffect effect, DamageSource source, float amount) {
        if (effect == StatusEffects.RESISTANCE && source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesResistance()) {
            return false;
        }

        return self.hasStatusEffect(effect);
    }

    @Redirect(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getProtectionAmount(Ljava/lang/Iterable;Lnet/minecraft/entity/damage/DamageSource;)I"))
    private int hatred$bypassesEnchantments(Iterable<ItemStack> equipment, DamageSource source) {
        if (source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesEnchantments()) {
            return 0;
        }

        return EnchantmentHelper.getProtectionAmount(equipment, source);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"), cancellable = true)
    private void hatred$nonFatal(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;

        if (!(source instanceof ContextAwareDamageSource ctx))
            return;

        if (!ctx.context().nonFatal())
            return;

        self.setHealth(1.0F);

        cir.setReturnValue(true);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeMovement(Vec3d movementInput, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;

        if(Data.API.get(self, HatredAttachments.MOVEMENT_FROZEN)) {
            self.setVelocity(Vec3d.ZERO);
            self.velocityModified = true;
            ci.cancel();
        }
    }
}
