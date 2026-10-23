package net.vinh.hatred.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.vinh.hatred.internal.scheduler.WorldScheduler;

public class HatredWorldComponent implements AutoSyncedComponent {
    private final World world;

    private WorldScheduler scheduler;

    public HatredWorldComponent(World world) {
        this.world = world;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {

    }

    public WorldScheduler getScheduler() {
        if(scheduler == null) {
            this.scheduler = new WorldScheduler();
        }

        return scheduler;
    }
}
