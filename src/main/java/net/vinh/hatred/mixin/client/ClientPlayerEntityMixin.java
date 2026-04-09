package net.vinh.hatred.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeDrop(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (Data.API.get(player, HatredAttachments.INVENTORY_FROZEN)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "setCurrentHand", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeDrop(Hand hand, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (Data.API.get(player, HatredAttachments.INVENTORY_FROZEN)) {
            ci.cancel();
        }
    }
}
