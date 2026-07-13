package net.vinh.hatred.api.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * A mutable version of {@link Args}
 */
public class MutableArgs extends Args {
    protected MutableArgs(ArrayList<Object> args) {
        super(args);
    }

    @Override
    public Args toImmutable() {
        return new Args(this.storedArgs);
    }

    public MutableArgs add(Object arg) {
        this.storedArgs.add(arg);
        return this;
    }

    public MutableArgs addAll(Object... args) {
        this.storedArgs.addAll(List.of(args));
        return this;
    }

    public MutableArgs set(int index, Object arg) {
        this.storedArgs.set(index, arg);
        return this;
    }
}
