package net.vinh.hatred.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.api.item.IConfigurableDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean hatred$attack(Entity instance, DamageSource source, float amount) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (player.getMainHandStack().getItem() instanceof IConfigurableDamageSource iChangeableDamageSource) {
            DamageSource custom = iChangeableDamageSource.createSource(player);

            DamageContextBuilder contextBuilder = new DamageContextBuilder()
                    .type(custom.getTypeRegistryEntry())
                    .attacker(custom.getAttacker())
                    .directSource(custom.getSource());

            if(iChangeableDamageSource.deathMessage() != null) {
                contextBuilder.deathMessage(iChangeableDamageSource.deathMessage());
            }

            if(iChangeableDamageSource.nonFatal()) {
                contextBuilder.nonFatal();
            }

            if(iChangeableDamageSource.trueDamage()) {
                contextBuilder.trueDamage();
            }

            if(iChangeableDamageSource.bypassArmor()) {
                contextBuilder.bypassesArmor();
            }

            if(iChangeableDamageSource.bypassResistance()) {
                contextBuilder.bypassesResistance();
            }

            if(iChangeableDamageSource.bypassEnchantments()) {
                contextBuilder.bypassesEnchantments();
            }

            if(iChangeableDamageSource.bypassInvulnerability()) {
                contextBuilder.bypassesInvulnerability();
            }

            if(iChangeableDamageSource.bypassTotems()) {
                contextBuilder.bypassesTotems();
            }

            if(iChangeableDamageSource.addKilledDisplayNameToMsg()) {
                contextBuilder.addKilledDisplayNameToMsg();
            }

            if(iChangeableDamageSource.hitEffects() != null) {
                contextBuilder.hitEffects(iChangeableDamageSource.hitEffects());
            }

            if(iChangeableDamageSource.knockback() != null) {
                contextBuilder.knockback(iChangeableDamageSource.knockback());
            }

            return instance.damage(amount, totalDamage -> totalDamage, contextBuilder.build());
        }

        return instance.damage(source, amount);
    }
}
