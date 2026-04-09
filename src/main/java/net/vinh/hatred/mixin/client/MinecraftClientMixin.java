package net.vinh.hatred.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @WrapOperation(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I"))
    private void hatred$freezeInventory(PlayerInventory instance, int value, Operation<Void> original) {
        if(Data.API.get(instance.player, HatredAttachments.INVENTORY_FROZEN)) return;
        original.call(instance, value);
    }
}
