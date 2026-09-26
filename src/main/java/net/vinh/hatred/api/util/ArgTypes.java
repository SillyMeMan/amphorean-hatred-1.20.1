package net.vinh.hatred.api.util;

public final class ArgTypes {
    public static ArgType<Byte> BYTE = ofClass(Byte.class);
    public static ArgType<Short> SHORT = ofClass(Short.class);
    public static ArgType<Integer> INTEGER = ofClass(Integer.class);
    public static ArgType<Long> LONG = ofClass(Long.class);
    public static ArgType<Float> FLOAT = ofClass(Float.class);
    public static ArgType<Double> DOUBLE = ofClass(Double.class);
    public static ArgType<Boolean> BOOLEAN = ofClass(Boolean.class);
    public static ArgType<Character> CHAR = ofClass(Character.class);
    public static ArgType<String> STRING = ofClass(String.class);

    public static <T> ArgType<T> ofClass(Class<T> type) {
        return new ClassArgType<>(type);
    }

    public static <T> ListArgType<T> ofList(Class<T> elementType) {
        return new ListArgType<>(ofClass(elementType));
    }
}
