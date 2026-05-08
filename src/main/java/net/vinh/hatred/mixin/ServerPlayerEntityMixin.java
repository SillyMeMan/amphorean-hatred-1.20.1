package net.vinh.hatred.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.api.ability.state.CombatStates;
import net.vinh.hatred.api.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "tickItemStackUsage", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeInteractItem(ItemStack stack, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (Data.API.get(player, CombatStates.MOVEMENT_FROZEN)) {
            ci.cancel();
        }
    }
}
