package net.vinh.hatred.api.data;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public interface DataAttachmentType<T> {
    Identifier id();
    Class<T> type();
    Supplier<T> factory();
    DataSerializer<T> serializer();
    boolean persistent();
    boolean syncable();
}

