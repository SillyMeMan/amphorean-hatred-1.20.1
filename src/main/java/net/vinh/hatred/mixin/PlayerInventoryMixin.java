package net.vinh.hatred.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.vinh.hatred.internal.ability.state.CombatStates;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @WrapMethod(method = "scrollInHotbar")
    private void hatred$freezeInventory(double scrollAmount, Operation<Void> original) {
        if(player.hasStatusEffect(CombatStates.MOVEMENT_FREEZE)) return;

        original.call(scrollAmount);
    }
}
