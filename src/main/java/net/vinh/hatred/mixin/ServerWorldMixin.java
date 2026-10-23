package net.vinh.hatred.mixin;

import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.internal.scheduler.Schedulers;
import net.vinh.hatred.internal.scheduler.WorldScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void hatred$tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld)(Object) this;

        WorldScheduler scheduler = Schedulers.world(world);
        scheduler.tick();
    }
}

