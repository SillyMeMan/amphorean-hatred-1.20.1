package net.vinh.hatred.api.util;

public final class ArgTypes {
    public static <T> ArgType<T> ofClass(Class<T> type) {
        return new ClassArgType<>(type);
    }

    public static <T> ListArgType<T> ofList(ArgType<T> elementType) {
        return new ListArgType<>(elementType);
    }
}
