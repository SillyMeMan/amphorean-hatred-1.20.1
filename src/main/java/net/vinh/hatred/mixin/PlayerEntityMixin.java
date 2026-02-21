package net.vinh.hatred.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.damage.DamageUtil;
import net.vinh.hatred.api.item.IConfigurableDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean hatred$attack(LivingEntity instance, DamageSource source, float amount) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.getMainHandStack().getItem() instanceof IConfigurableDamageSource iChangeableDamageSource) {
            DamageSource custom = iChangeableDamageSource.createSource(player);

            DamageContext.Builder contextBuilder = DamageContext.builder()
                    .attacker(custom.getAttacker());

            if(iChangeableDamageSource.bypassArmor()) {
                contextBuilder.bypassesArmor();
            }

            if(iChangeableDamageSource.bypassResistance()) {
                contextBuilder.bypassesResistance();
            }

            if(iChangeableDamageSource.bypassInvulnerability()) {
                contextBuilder.bypassesInvulnerability();
            }

            if(iChangeableDamageSource.bypassTotems()) {
                contextBuilder.bypassesTotems();
            }

            if(iChangeableDamageSource.hitEffects() != null) {
                contextBuilder.hitEffects(iChangeableDamageSource.hitEffects());
            }

            if(iChangeableDamageSource.knockback() != null) {
                contextBuilder.knockback(iChangeableDamageSource.knockback());
            }

            return DamageUtil.apply(instance, contextBuilder.build());
        }

        return instance.damage(source, amount);
    }
}
