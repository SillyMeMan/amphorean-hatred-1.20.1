package net.vinh.hatred.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.internal.ability.state.CombatStates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderPearlItem.class)
public abstract class EnderPearlItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void hatred$blockEnderPearlUsage(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if(user.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
        }
    }
}
