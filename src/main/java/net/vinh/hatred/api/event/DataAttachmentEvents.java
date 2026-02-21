package net.vinh.hatred.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.vinh.hatred.api.data.DataAttachmentType;

public final class DataAttachmentEvents {
    public static final Event<OnChanged> ON_CHANGED = EventFactory.createArrayBacked(OnChanged.class, callbacks -> (holder, type) -> {
       for(OnChanged callback : callbacks) {
           callback.onChanged(holder, type);
       }
    });

    @FunctionalInterface
    public interface OnChanged {
        void onChanged(Object holder, DataAttachmentType<?> type);
    }
}
