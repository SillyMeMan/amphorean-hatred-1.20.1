package net.vinh.hatred.internal.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class HatredWorldState extends PersistentState {

    private final DataContainer container = new DataContainer();

    public DataContainer getContainer() {
        return container;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        container.writePersistent(nbt);
        return nbt;
    }

    public static HatredWorldState fromNbt(NbtCompound nbt) {
        HatredWorldState state = new HatredWorldState();
        state.container.readPersistent(nbt);
        return state;
    }
}
