package net.vinh.hatred.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class WorldBossEvents {
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

    public static final Event<DragonKilled> DRAGON_KILLED = EventFactory.createArrayBacked(DragonKilled.class, callbacks -> (world, finalKiller, firstDragon) -> {
       for(DragonKilled killed : callbacks) {
           killed.onDragonKilled(world, finalKiller, firstDragon);
       }
    });

    public static final Event<DragonRespawn> DRAGON_RESPAWN = EventFactory.createArrayBacked(DragonRespawn.class, callbacks -> world -> {
        for(DragonRespawn respawn : callbacks) {
            respawn.onStartingRespawnSequence(world);
        }
    });

    public static final Event<DragonRespawnAborted> DRAGON_RESPAWN_ABORTED = EventFactory.createArrayBacked(DragonRespawnAborted.class, callbacks -> (world, destroyedCrystal) -> {
        for(DragonRespawnAborted aborted : callbacks) {
            aborted.onRespawnAborted(world, destroyedCrystal);
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
        void onDragonKilled(World world, LivingEntity finalKiller, boolean firstDragon);
    }

    @FunctionalInterface
    public interface DragonRespawn {
        void onStartingRespawnSequence(World world);
    }

    @FunctionalInterface
    public interface DragonRespawnAborted {
        void onRespawnAborted(World world, EndCrystalEntity destroyedCrystal);
    }

    private WorldBossEvents() {}
}
