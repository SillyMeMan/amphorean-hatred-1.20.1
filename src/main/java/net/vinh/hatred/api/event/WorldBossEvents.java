package net.vinh.hatred.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldBossEvents {
    public static final Event<WitherSpawn> WITHER_SPAWN = EventFactory.createArrayBacked(WitherSpawn.class, callbacks -> (world, pos) -> {
        for(WitherSpawn spawn : callbacks) {
            spawn.onWitherSpawn(world, pos);
        }
    });

    public static final Event<DragonSpawn> DRAGON_SPAWN = EventFactory.createArrayBacked(DragonSpawn.class, callbacks -> (world, pos, firstDragon) -> {
       for(DragonSpawn spawn : callbacks) {
           spawn.onDragonSpawn(world, pos, firstDragon);
       }
    });

    public static final Event<DragonKilled> DRAGON_KILLED = EventFactory.createArrayBacked(DragonKilled.class, callbacks -> (world, finalKiller) -> {
       for(DragonKilled killed : callbacks) {
           killed.onDragonKilled(world, finalKiller);
       }
    });

    @FunctionalInterface
    public interface WitherSpawn {
        void onWitherSpawn(World world, BlockPos pos);
    }

    @FunctionalInterface
    public interface DragonSpawn {
        void onDragonSpawn(World world, BlockPos pos, boolean firstDragon);
    }

    @FunctionalInterface
    public interface DragonKilled {
        void onDragonKilled(World world, LivingEntity finalKiller);
    }

    private WorldBossEvents() {}
}
