package net.vinh.hatred.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.internal.ability.state.CombatStates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "tickItemStackUsage", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeInteractItem(ItemStack stack, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (player.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) {
            ci.cancel();
        }
    }
}
