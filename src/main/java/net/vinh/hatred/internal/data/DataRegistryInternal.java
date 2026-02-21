package net.vinh.hatred.internal.data;

import net.minecraft.util.Identifier;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataSerializer;
import net.vinh.hatred.impl.data.DataAttachmentTypeImpl;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class DataRegistryInternal {

    private static final Map<Identifier, DataAttachmentType<?>> REGISTRY = new HashMap<>();
    private static boolean frozen = false;

    public static <T> DataAttachmentType<T> register(Identifier id, Class<T> type, Supplier<T> factory, DataSerializer<T> serializer, boolean persistent, boolean syncable) {
        if (frozen) throw new IllegalStateException("Registry frozen");

        if (REGISTRY.containsKey(id))
            throw new IllegalArgumentException("Duplicate attachment id: " + id);

        DataAttachmentType<T> attachment = new DataAttachmentTypeImpl<>(id, type, factory, serializer, persistent, syncable);

        REGISTRY.put(id, attachment);
        return attachment;
    }

    public static DataAttachmentType<?> get(Identifier id) {
        return REGISTRY.get(id);
    }


    public static void freeze() {
        frozen = true;
    }

    public static void unfreeze() {
        frozen = false;
    }

    public static Collection<DataAttachmentType<?>> values() {
        return REGISTRY.values();
    }
}

