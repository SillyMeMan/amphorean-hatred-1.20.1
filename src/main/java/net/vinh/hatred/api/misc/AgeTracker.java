package net.vinh.hatred.api.misc;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public final class AgeTracker {
    private long createdTick = -1;

    public void initialize(ServerWorld world) {
        if (createdTick == -1) {
            createdTick = world.getTime();
        }
    }

    public long age(ServerWorld world) {
        initialize(world);
        return world.getTime() - createdTick;
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putLong("CreatedTick", createdTick);
    }

    public void readNbt(NbtCompound nbt) {
        createdTick = nbt.getLong("CreatedTick");
    }
}
