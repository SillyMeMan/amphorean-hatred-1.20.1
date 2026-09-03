package net.vinh.hatred.api.util;

public interface ArgType<T> {
    boolean matches(Object value);

    T cast(Object value);

    default T castOrThrow(Object value) throws IllegalArgumentException {
        if(!matches(value)) {
            throw new IllegalArgumentException(
                    "Invalid argument type. Expected " +
                            this +
                            ", got " +
                            (value == null ? "null" : value.getClass().getName())
            );
        }

        return cast(value);
    }
}
