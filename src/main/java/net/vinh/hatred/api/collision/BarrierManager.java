package net.vinh.hatred.api.collision;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BarrierManager {
private static final Map<RegistryKey<World>, Barrier> barriers = new HashMap<>(); // TODO: make this system resistant to world rejoins/server restarts

    public static void addBarrier(World dimension, Barrier barrier) {
        barriers.put(dimension.getRegistryKey(), barrier);
    }

    public static void removeBarrier(World dimension, Barrier barrier) {
        barriers.remove(dimension.getRegistryKey(), barrier);
    }

    public static Map<RegistryKey<World>, Barrier> getCurrentBarriers() {
        return barriers;
    }
}
