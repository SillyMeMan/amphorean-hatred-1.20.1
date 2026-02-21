package net.vinh.hatred.impl.data;

import net.vinh.hatred.api.data.DataApi;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class DataApiImpl implements DataApi {
    @Override
    public <T> T get(Object holder, DataAttachmentType<T> type) {
        DataContainer container = resolveContainer(holder);
        return container.get(type);
    }

    @Override
    public <T> void set(Object holder, DataAttachmentType<T> type, T value) {
        resolveContainer(holder).set(type, value);
    }

    @Override
    public void remove(Object holder, DataAttachmentType<?> type) {
        resolveContainer(holder).remove(type);
    }

    @Override
    public <T> void mutate(Object holder, DataAttachmentType<T> type, Consumer<T> mutator) {
        DataContainer container = resolveContainer(holder);

        T value = container.get(type); // lazy init guaranteed

        mutator.accept(value);

        container.markDirty(type);
    }

    @Override
    public <T> T compute(Object holder, DataAttachmentType<T> type, UnaryOperator<T> operator) {
        DataContainer container = resolveContainer(holder);

        T oldValue = container.get(type);
        T newValue = operator.apply(oldValue);

        container.set(type, newValue);

        return newValue;
    }

    private DataContainer resolveContainer(Object holder) {
        if (holder instanceof DataHolderInternal internal)
            return internal.hatred$getContainer();

        throw new IllegalArgumentException("Unsupported holder: " + holder);
    }
}
