package net.vinh.hatred.api.data;

import net.minecraft.nbt.NbtCompound;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class DataSerializers {
    private DataSerializers() {}

    public static final DataSerializer<Integer> INT =
            DataSerializers.of(
                    (nbt, value) -> nbt.putInt("v", value),
                    nbt -> nbt.getInt("v")
            );

    public static final DataSerializer<Boolean> BOOLEAN =
            DataSerializers.of(
                    (nbt, value) -> nbt.putBoolean("v", value),
                    nbt -> nbt.getBoolean("v")
            );

    public static final DataSerializer<Long> LONG =
            DataSerializers.of(
                    (nbt, value) -> nbt.putLong("v", value),
                    nbt -> nbt.getLong("v")
            );

    public static final DataSerializer<Float> FLOAT =
            DataSerializers.of(
                    (nbt, value) -> nbt.putFloat("v", value),
                    nbt -> nbt.getFloat("v")
            );

    public static final DataSerializer<Double> DOUBLE =
            DataSerializers.of(
                    (nbt, value) -> nbt.putDouble("v", value),
                    nbt -> nbt.getDouble("v")
            );

    public static final DataSerializer<String> STRING =
            DataSerializers.of(
                    (nbt, value) -> nbt.putString("v", value),
                    nbt -> nbt.getString("v")
            );

    public static final DataSerializer<UUID> UUID =
            DataSerializers.of(
                    (nbt, value) -> nbt.putUuid("v", value),
                    nbt -> nbt.getUuid("v")
            );

    public static <T extends Enum<T>> DataSerializer<T> enumSerializer(Class<T> enumClass) {
        return DataSerializers.of(
                (nbt, value) -> nbt.putString("v", value.name()),
                nbt -> Enum.valueOf(enumClass, nbt.getString("v"))
        );
    }

    public static <T> DataSerializer<T> of(BiConsumer<NbtCompound, T> writer, Function<NbtCompound, T> reader) {
        return new DataSerializer<>() {
            @Override
            public void writeNbt(NbtCompound nbt, T value) {
                writer.accept(nbt, value);
            }

            @Override
            public T readNbt(NbtCompound nbt) {
                return reader.apply(nbt);
            }
        };
    }
}
