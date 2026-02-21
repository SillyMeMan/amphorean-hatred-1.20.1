package net.vinh.hatred;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.scheduler.WorldScheduler;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.attachment.HatredDataAttachmentTypes;
import net.vinh.hatred.client.camera.ScreenshakeController;
import net.vinh.hatred.impl.TestItem;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.accessor.EntityMixinAccessor;
import net.vinh.hatred.networking.packet.AltAbilityC2SPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.vinh.hatred.attachment.HatredDataAttachmentTypes.ENTITY_SCHEDULER;

public class AmphoreanHatred implements ModInitializer {
	public static final String MOD_ID = "hatred";
	public static final Logger LOGGER = LoggerFactory.getLogger("Amphorean Hatred");

	@Override
	public void onInitialize() {
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			WorldScheduler scheduler = Data.API.get(world, HatredDataAttachmentTypes.WORLD_SCHEDULER);
			scheduler.tick();
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> ScreenshakeController.tick());
		ServerPlayNetworking.registerGlobalReceiver(AltAbilityC2SPacket.ID, AltAbilityC2SPacket::handle);

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (world.isClient()) return;

			if (!(entity instanceof DataHolderInternal)) return;

			for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
				((EntityMixinAccessor) entity).hatred$syncFull(player);
			}
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			Data.API.get(entity, ENTITY_SCHEDULER).clear();
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			if (player instanceof EntityMixinAccessor sync) {
				sync.hatred$syncFull(player);
			}
		});

		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (!(oldPlayer instanceof DataHolderInternal oldHolder)) return;
			if (!(newPlayer instanceof DataHolderInternal newHolder)) return;

			DataContainer oldContainer = oldHolder.hatred$getContainer();
			DataContainer newContainer = newHolder.hatred$getContainer();

			for (var entry : oldContainer.entries().entrySet()) {
				DataAttachmentType<?> type = entry.getKey();

				if (!type.persistent()) continue;

				newContainer.setRaw(type, entry.getValue());
			}
		});

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			Data.API.get(entity, ENTITY_SCHEDULER).clear();
		});

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registries.ITEM, new Identifier(MOD_ID, "test"), new TestItem(new FabricItemSettings()));
		}
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}