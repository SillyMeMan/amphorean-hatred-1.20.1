package net.vinh.hatred.mixin;

import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.internal.HatredInternalAttachments;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.HatredWorldState;
import net.vinh.hatred.internal.scheduler.WorldScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements DataHolderInternal {
    @Unique
    private HatredWorldState hatred$state;

    @Override
    public DataContainer hatred$getContainer() {
        if (hatred$state == null) {
            ServerWorld world = (ServerWorld)(Object)this;

            hatred$state = world.getPersistentStateManager()
                    .getOrCreate(
                            HatredWorldState::fromNbt,
                            HatredWorldState::new,
                            "hatred_world_data"
                    );
        }

        return hatred$state.getContainer();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hatred$tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld)(Object) this;

        WorldScheduler scheduler = Data.API.get(world, HatredInternalAttachments.WORLD_SCHEDULER);
        scheduler.tick();
    }
}

