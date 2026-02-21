package net.vinh.hatred.api.data;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface DataApi {
    <T> T get(Object holder, DataAttachmentType<T> type);
    <T> void set(Object holder, DataAttachmentType<T> type, T value);
    void remove(Object holder, DataAttachmentType<?> type);
    <T> void mutate(Object holder, DataAttachmentType<T> type, Consumer<T> mutator);
    <T> T compute(Object holder, DataAttachmentType<T> type, UnaryOperator<T> operator);
}
