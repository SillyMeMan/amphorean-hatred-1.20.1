package net.vinh.hatred.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.vinh.hatred.exception.ManuallyThrownCrashException;
import net.vinh.hatred.internal.ability.state.CombatStates;
import net.vinh.hatred.client.animation.AnimationManager;
import net.vinh.hatred.client.camera.ScreenshakeController;
import net.vinh.hatred.internal.util.ClientCrashHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @WrapOperation(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void hatred$freezeInventory(PlayerInventory instance, int value, Operation<Void> original) {
        if(instance.player.hasStatusEffect(CombatStates.INVENTORY_FREEZE)) return;
        original.call(instance, value);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hatred$tick(CallbackInfo ci) {
        AnimationManager.tick();
        ScreenshakeController.tick();

        if(ClientCrashHandler.shouldCrash) {
            CrashReport crashReport = new CrashReport(ClientCrashHandler.reason, new ManuallyThrownCrashException(ClientCrashHandler.reason));
            CrashReportSection crashReportSection = crashReport.addElement("Crash details");
            WinNativeModuleUtil.addDetailTo(crashReportSection);
            throw new CrashException(crashReport);
        }
    }
}
