package net.vinh.hatred.api.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * An argument storer that is intended to be used for storing arguments used by {@link net.vinh.hatred.api.ability.Ability}.
 * <p>
 * Can be adapted by other libraries for various features.
 */
public class Args implements Iterable<Object> {
    protected final List<Object> storedArgs;

    protected Args(List<Object> args) {
        this.storedArgs = args;
    }

    @Contract("_ -> new")
    public static @NotNull MutableArgs ofMutable(@NotNull Object... args) {
        return new MutableArgs(new ArrayList<>(List.of(args)));
    }

    @Contract("_ -> new")
    public static @NotNull Args ofImmutable(@NotNull Object... args) {
        return new Args(List.of(args));
    }

    public Args toImmutable() {
        return this;
    }

    /**
     * Get the argument currently stored at the specified index. If the result this not an instance of the type, it will throw an {@link IllegalStateException}. The index is zero-based.
     * @param index The index.
     * @param type The {@link ArgType} of the expected type
     * @return The argument currently stored at specified index.
     * @param <T> A type variable specifying the wanted return type.
     * @throws IllegalStateException if the return value is not the same as the type
     */
    public <T> T getOrThrow(int index, ArgType<T> type) {
        return getOptional(index, type).orElseThrow(() -> new IllegalStateException(
                "Argument at index " + index +
                        " does not match expected type " + type
        ));
    }

    /**
     * Get the argument currently stored at the specified index. If the result this not equal to the expected type, it will return an empty {@link Optional}. The index is zero-based.
     * @param index The index.
     * @param type The {@link ArgType} of the expected type
     * @return The argument currently stored at specified index.
     * @param <T> A type variable specifying the wanted return type.
     */
    public <T> Optional<T> getOptional(int index, ArgType<T> type) {
        if (index < 0 || index >= storedArgs.size()) {
            throw new IndexOutOfBoundsException(
                    "Argument index " + index +
                            " is out of bounds. Argument count: " +
                            storedArgs.size()
            );
        }

        try {
            return Optional.of(type.cast(storedArgs.get(index)));
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Returns the argument list size/length
     * @return The argument list size/length
     */
    public int size() {
        return storedArgs.size();
    }

    @Override
    public @NotNull Iterator<Object> iterator() {
        return storedArgs.listIterator();
    }
}
