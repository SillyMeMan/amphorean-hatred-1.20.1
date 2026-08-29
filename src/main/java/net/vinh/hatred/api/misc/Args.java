package net.vinh.hatred.api.misc;

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
     * Get the argument currently stored at the specified index. If the result this not an instance of the expected type, it will throw an {@link IllegalStateException}. The index is zero-based.
     * @param index The index.
     * @param expectedType The class of the expected time
     * @return The argument currently stored at specified index.
     * @param <T> A type variable specifying the wanted return type.
     * @throws IllegalStateException if the return value is not the same as the expectedType
     */
    public <T> T getOrThrow(int index, Class<T> expectedType) {
        return getOptional(index, expectedType).orElseThrow(() -> new IllegalStateException("The return value is not the same as the expectedType"));
    }

    /**
     * Get the argument currently stored at the specified index. If the result this not equal to the expected type, it will return an empty {@link Optional}. The index is zero-based.
     * @param index The index.
     * @param expectedType The class of the expected time
     * @return The argument currently stored at specified index.
     * @param <T> A type variable specifying the wanted return type.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(int index, Class<T> expectedType) {
        try {
            Object stored = storedArgs.get(index);

            if(!expectedType.isInstance(stored)) {
                return Optional.empty();
            } else {
                return Optional.of((T) stored);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(e);
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
