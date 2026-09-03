package net.vinh.hatred.api.util;

public class ClassArgType<T> implements ArgType<T> {
    private final Class<T> type;

    public ClassArgType(Class<T> type) {
        this.type = type;
    }

    @Override
    public boolean matches(Object value) {
        return type.isInstance(value);
    }

    @Override
    public T cast(Object value) {
        return type.cast(value);
    }

    @Override
    public String toString() {
        return type.getTypeName();
    }
}
