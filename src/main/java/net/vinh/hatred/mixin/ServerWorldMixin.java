package net.vinh.hatred.mixin;

import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.HatredWorldState;
import net.vinh.hatred.internal.world.WorldInjectionAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements DataHolderInternal {
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

    @Unique
    public void hatred$markDirty() {
        if (hatred$state != null) {
            hatred$state.markDirty();
        }
    }
}

