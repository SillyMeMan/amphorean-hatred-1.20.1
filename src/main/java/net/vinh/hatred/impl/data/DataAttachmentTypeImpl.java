package net.vinh.hatred.impl.data;

import net.minecraft.util.Identifier;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataSerializer;

import java.util.function.Supplier;

public record DataAttachmentTypeImpl<T>(Identifier id, Class<T> type, Supplier<T> factory, DataSerializer<T> serializer, boolean persistent, boolean syncable) implements DataAttachmentType<T> {}
