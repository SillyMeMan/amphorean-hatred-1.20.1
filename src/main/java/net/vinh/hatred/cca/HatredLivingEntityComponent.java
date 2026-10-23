package net.vinh.hatred.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.CooldownEntry;
import net.vinh.hatred.internal.scheduler.EntityScheduler;

import java.util.HashMap;
import java.util.Map;

public class HatredLivingEntityComponent implements AutoSyncedComponent {
    private final LivingEntity entity;

    private EntityScheduler scheduler;
    private final Map<Identifier, CooldownEntry> cooldowns = new HashMap<>();

    public boolean isUsingAbility = false;
    public boolean noStun = false;

    public boolean movementFrozen = false;
    public boolean rotationLocked = false;

    public boolean isInvulnerable = false;
    public boolean isPureInvulnerable = false;

    public HatredLivingEntityComponent(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        cooldowns.clear();

        NbtCompound cooldownNbt = nbt.getCompound("cooldowns");

        for (String key : cooldownNbt.getKeys()) {
            Identifier id = Identifier.tryParse(key);

            if (id == null) {
                continue;
            }

            NbtCompound sub = cooldownNbt.getCompound(key);
            cooldowns.put(id, new CooldownEntry(sub.getLong("ready")));
        }

        movementFrozen = nbt.getBoolean("movement_frozen");
        rotationLocked = nbt.getBoolean("rotation_locked");

        isInvulnerable = nbt.getBoolean("is_invulnerable");
        isPureInvulnerable = nbt.getBoolean("is_pure_invulnerable");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        NbtCompound cooldownNbt = new NbtCompound();

        for (var entry : cooldowns.entrySet()) {
            NbtCompound sub = new NbtCompound();
            sub.putLong("ready", entry.getValue().readyTick);
            cooldownNbt.put(entry.getKey().toString(), sub);
        }

        nbt.put("cooldowns", cooldownNbt);

        nbt.putBoolean("movement_frozen", movementFrozen);
        nbt.putBoolean("rotation_locked", rotationLocked);

        nbt.putBoolean("is_invulnerable", isInvulnerable);
        nbt.putBoolean("is_pure_invulnerable", isPureInvulnerable);
    }

    public EntityScheduler getScheduler() {
        if(scheduler == null) {
            this.scheduler = new EntityScheduler();
        }

        return scheduler;
    }

    public Map<Identifier, CooldownEntry> getCooldowns() {
        return cooldowns;
    }
}
