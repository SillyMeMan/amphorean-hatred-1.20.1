package net.vinh.hatred.api.data;

import net.minecraft.nbt.NbtCompound;

public interface DataSerializer<T> {
    void writeNbt(NbtCompound tag, T value);
    T readNbt(NbtCompound tag);
}

