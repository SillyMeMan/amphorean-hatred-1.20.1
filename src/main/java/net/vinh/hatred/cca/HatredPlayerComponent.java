package net.vinh.hatred.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class HatredPlayerComponent implements AutoSyncedComponent {
    private final PlayerEntity player;

    public boolean inventoryFrozen = false;

    public HatredPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.inventoryFrozen = nbtCompound.getBoolean("inventory_frozen");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.putBoolean("inventory_frozen", this.inventoryFrozen);
    }
}
