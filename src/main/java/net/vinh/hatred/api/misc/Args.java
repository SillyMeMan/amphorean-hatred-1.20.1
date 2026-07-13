package net.vinh.hatred.api.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * An argument storer that is intended to be used for storing arguments used by {@link net.vinh.hatred.api.ability.Ability}.
 * <p>
 * Can be used by other libraries for various features.
 */
public class Args {
    protected final ArrayList<Object> storedArgs;

    protected Args(ArrayList<Object> args) {
        this.storedArgs = args;
    }

    public static Args of(Object... args) {
        return new MutableArgs(new ArrayList<>(List.of(args)));
    }

    public Args toImmutable() {
        return this;
    }

    /**
     * Get the argument currently stored at the specified index. The index is zero-based.
     * @param index The index.
     * @return The argument currently stored at specified index.
     * @param <T> A type variable specifying the wanted return type.
     * @throws ClassCastException If the fetched argument is not the same as the type variable.
     * @throws IndexOutOfBoundsException If the index is out of the argument list range
     */
    public <T> T get(int index) {
        return (T) storedArgs.get(index);
    }

    /**
     * Returns the argument list size/length
     * @return The argument list size/length
     */
    public int size() {
        return storedArgs.size();
    }
}
