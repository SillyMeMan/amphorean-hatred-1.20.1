package net.vinh.hatred.internal.data;

import net.minecraft.nbt.NbtCompound;
import net.vinh.hatred.api.data.DataAttachmentType;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApiStatus.Internal
public final class DataContainer {

    private final Map<DataAttachmentType<?>, Object> values = new HashMap<>();
    private final Set<DataAttachmentType<?>> dirty = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <T> T get(DataAttachmentType<T> type) {
        Object value = values.get(type);

        if (value == null) {
            T created = type.factory().get();
            values.put(type, created);
            return created;
        }

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrNull(DataAttachmentType<T> type) {
        Object value = values.get(type);
        if(value == null) {
            return null;
        }
        return (T) value;
    }

    public <T> void set(DataAttachmentType<T> type, T value) {
        values.put(type, value);
        markDirty(type);
    }

    public void remove(DataAttachmentType<?> type) {
        values.remove(type);
        markDirty(type);
    }

    public void setRaw(DataAttachmentType<?> type, Object value) {
        values.put(type, value);
        markDirty(type);
    }

    public Map<DataAttachmentType<?>, Object> entries() {
        return values;
    }

    public Set<DataAttachmentType<?>> consumeDirty() {
        Set<DataAttachmentType<?>> copy = new HashSet<>(dirty);
        dirty.clear();
        return copy;
    }

    public void markDirty(DataAttachmentType<?> type) {
        if (type.syncable()) {
            dirty.add(type);
        }
    }

    public void writePersistent(NbtCompound nbt) {
        NbtCompound root = new NbtCompound();

        for (var entry : values.entrySet()) {
            DataAttachmentType<?> type = entry.getKey();

            if (!type.persistent()) continue;

            NbtCompound sub = new NbtCompound();
            writePersistentAttachment(type, entry.getValue(), sub);

            root.put(type.id().toString(), sub);
        }

        nbt.put("hatred:data", root);
    }

    public void readPersistent(NbtCompound nbt) {
        if (!nbt.contains("hatred:data")) return;

        NbtCompound root = nbt.getCompound("hatred:data");

        for (DataAttachmentType<?> type : DataRegistryInternal.values()) {
            if (!type.persistent()) continue;

            String key = type.id().toString();

            if (root.contains(key)) {
                NbtCompound sub = root.getCompound(key);
                Object value = type.serializer().readNbt(sub);
                values.put(type, value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void writePersistentAttachment(DataAttachmentType<T> type, Object value, NbtCompound tag) {
        type.serializer().writeNbt(tag, (T) value);
    }

    public Map<DataAttachmentType<?>, Object> getAllSyncable() {
        return values.entrySet().stream()
                .filter(e -> e.getKey().syncable())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}

