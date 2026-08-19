package net.vinh.hatred.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.internal.ability.state.CombatStates;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.internal.entity.LivingEntityInjectionAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityInjectionAccess {
    @Shadow public abstract int getArmor();

    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow public abstract void damageArmor(DamageSource source, float amount);

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void hatred$bypassesTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesTotems()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
    private void hatred$applyArmorRelatedOptions(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if(source instanceof ContextAwareDamageSource ctx) {
            if(ctx.context().bypassesArmor()) {
                cir.setReturnValue(amount);
                return;
            }

            damageArmor(source, amount);

            float multiplier = ctx.context().armorEffectivenessMultiplier();

            float totalArmor = getArmor() * multiplier;
            double armorToughness = getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);

            cir.setReturnValue(DamageUtil.getDamageLeft(amount, totalArmor, (float) armorToughness));
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

        if(source instanceof ContextAwareDamageSource ctx && ctx.context().nonFatal()) {
            self.setHealth(1f);
            cir.setReturnValue(true);
        }
    }

    @ModifyExpressionValue(method = "applyDamage", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"))
    private float hatred$bypassesAbsorption(float original, DamageSource source, float amount) {
        if(source instanceof ContextAwareDamageSource ctx && ctx.context().bypassesAbsorption()) {
            return amount;
        }

        return original;
    }

    @Inject(method = "isUsingItem", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeItemUse(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void hatred$frozen(Vec3d movementInput, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if(self.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            self.setVelocity(Vec3d.ZERO);
            self.velocityModified = true;
            ci.cancel();
        }
    }
}
