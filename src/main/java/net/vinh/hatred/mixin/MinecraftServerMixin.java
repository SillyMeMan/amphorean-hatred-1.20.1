package net.vinh.hatred.mixin;

import net.minecraft.server.MinecraftServer;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.HatredServerState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements DataHolderInternal {
    @Unique
    private HatredServerState hatred$state;

    @Override
    public DataContainer hatred$getContainer() {
        if (hatred$state == null) {
            MinecraftServer server = (MinecraftServer)(Object)this;

            hatred$state = server.getOverworld()
                    .getPersistentStateManager()
                    .getOrCreate(
                            HatredServerState::fromNbt,
                            HatredServerState::new,
                            "hatred_server_data"
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
