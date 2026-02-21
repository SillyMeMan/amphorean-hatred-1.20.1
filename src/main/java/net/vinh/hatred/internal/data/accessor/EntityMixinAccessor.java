package net.vinh.hatred.internal.data.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface EntityMixinAccessor {
    void hatred$syncFull(ServerPlayerEntity player);
}
