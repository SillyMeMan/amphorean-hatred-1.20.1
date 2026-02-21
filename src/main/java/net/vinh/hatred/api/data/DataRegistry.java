package net.vinh.hatred.api.data;

import net.minecraft.util.Identifier;
import net.vinh.hatred.internal.data.DataRegistryInternal;

import java.util.UUID;
import java.util.function.Supplier;

public final class DataRegistry {
    public static <T> DataAttachmentType<T> register(Identifier id, Class<T> type, Supplier<T> factory, DataSerializer<T> serializer, boolean persistent, boolean syncable) {
        return DataRegistryInternal.register(id, type, factory, serializer, persistent, syncable);
    }

    public static DataAttachmentType<Integer> registerInt(Identifier id, Supplier<Integer> factory, boolean persistent, boolean syncable) {
        return register(id, Integer.class, factory, DataSerializers.INT, persistent, syncable);
    }

    public static DataAttachmentType<Boolean> registerBoolean(Identifier id, Supplier<Boolean> factory, boolean persistent, boolean syncable) {
        return register(id, Boolean.class, factory, DataSerializers.BOOLEAN, persistent, syncable);
    }

    public static DataAttachmentType<Double> registerDouble(Identifier id, Supplier<Double> factory, boolean persistent, boolean syncable) {
        return register(id, Double.class, factory, DataSerializers.DOUBLE, persistent, syncable);
    }

    public static DataAttachmentType<Long> registerLong(Identifier id, Supplier<Long> factory, boolean persistent, boolean syncable) {
        return register(id, Long.class, factory, DataSerializers.LONG, persistent, syncable);
    }

    public static DataAttachmentType<Float> registerFloat(Identifier id, Supplier<Float> factory, boolean persistent, boolean syncable) {
        return register(id, Float.class, factory, DataSerializers.FLOAT, persistent, syncable);
    }

    public static DataAttachmentType<String> registerString(Identifier id, Supplier<String> factory, boolean persistent, boolean syncable) {
        return register(id, String.class, factory, DataSerializers.STRING, persistent, syncable);
    }

    public static DataAttachmentType<UUID> registerUuid(Identifier id, Supplier<UUID> factory, boolean persistent, boolean syncable) {
        return register(id, UUID.class, factory, DataSerializers.UUID, persistent, syncable);
    }

    public static <T extends Enum<T>> DataAttachmentType<T> registerEnum(Identifier id, Supplier<T> factory, Class<T> enumClass, boolean persistent, boolean syncable) {
        return register(id, enumClass, factory, DataSerializers.enumSerializer(enumClass), persistent, syncable);
    }

    public static <T> DataAttachmentType<T> registerObject(Identifier id, Class<T> typeClass, Supplier<T> factory, DataSerializer<T> serializer, boolean persistent, boolean syncable) {
        return register(id, typeClass, factory, serializer, persistent, syncable);
    }
}
