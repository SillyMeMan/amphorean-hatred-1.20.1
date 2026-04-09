package net.vinh.hatred.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredAttachments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @WrapMethod(method = "scrollInHotbar")
    private void hatred$freezeInventory(double scrollAmount, Operation<Void> original) {
        if(Data.API.get(player, HatredAttachments.INVENTORY_FROZEN)) return;

        original.call(scrollAmount);
    }
}
