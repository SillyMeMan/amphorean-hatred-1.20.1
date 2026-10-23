package net.vinh.hatred.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.vinh.hatred.AmphoreanHatred;

public class ModComponents implements WorldComponentInitializer, EntityComponentInitializer {
    public static final ComponentKey<HatredLivingEntityComponent> LIVING_ENTITY = ComponentRegistry.getOrCreate(AmphoreanHatred.id("living_component"), HatredLivingEntityComponent.class);
    public static final ComponentKey<HatredWorldComponent> WORLD = ComponentRegistry.getOrCreate(AmphoreanHatred.id("world_component"), HatredWorldComponent.class);
    public static final ComponentKey<HatredPlayerComponent> PLAYER = ComponentRegistry.getOrCreate(AmphoreanHatred.id("player_component"), HatredPlayerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.beginRegistration(LivingEntity.class, LIVING_ENTITY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(HatredLivingEntityComponent::new);
        entityComponentFactoryRegistry.registerForPlayers(PLAYER, HatredPlayerComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry worldComponentFactoryRegistry) {
        worldComponentFactoryRegistry.register(WORLD, HatredWorldComponent::new);
    }
}
