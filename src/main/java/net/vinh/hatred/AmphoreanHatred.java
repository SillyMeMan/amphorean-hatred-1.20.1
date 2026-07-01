package net.vinh.hatred;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.brigadier.arguments.AbilityArgumentType;
import net.vinh.hatred.api.command.AbilityCommand;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.ability.state.CombatStates;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.impl.TestItem;
import net.vinh.hatred.internal.AutoRegistry;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.accessor.EntityMixinAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.vinh.hatred.internal.HatredInternalAttachments.ENTITY_SCHEDULER;

public class AmphoreanHatred implements ModInitializer {
	public static final String MOD_ID = "hatred";
	public static final Logger LOGGER = LoggerFactory.getLogger("Amphorean Hatred");

	@Override
	public void onInitialize() {
		HatredRegistries.initRegistries();
        AutoRegistry.autoCommonBootstrap();
        AbilityArgumentType.init();

		CommandRegistrationCallback.EVENT.register(AbilityCommand::register);

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (world.isClient()) return;

			if (!(entity instanceof DataHolderInternal)) return;

			for (ServerPlayerEntity player : PlayerLookup.tracking(entity)) {
				((EntityMixinAccessor) entity).hatred$syncFull(player);
			}
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> Data.API.get(entity, ENTITY_SCHEDULER).clear());

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			if (player instanceof EntityMixinAccessor sync) {
				sync.hatred$syncFull(player);
			}

			// just to prevent anyone from cheating the system
			for(Identifier abilityId : HatredRegistries.ABILITY.getIds()) {
				player.setCooldown(HatredRegistries.ABILITY.get(abilityId), HatredRegistries.ABILITY.get(abilityId).cooldown());
			}

			// to prevent bugs
			player.removeStatusEffect(CombatStates.INVENTORY_FREEZE);
			player.removeStatusEffect(CombatStates.ROTATION_LOCK);
			player.removeStatusEffect(CombatStates.MOVEMENT_FREEZE);
		});

		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (alive) return;

			if (!(oldPlayer instanceof DataHolderInternal oldHolder)) return;
			if (!(newPlayer instanceof DataHolderInternal newHolder)) return;

			DataContainer oldContainer = oldHolder.hatred$getContainer();
			DataContainer newContainer = newHolder.hatred$getContainer();

			for (var entry : oldContainer.entries().entrySet()) {

				DataAttachmentType type = entry.getKey();

				if (!type.persistent()) continue;

				Object value = entry.getValue();

				NbtCompound temp = new NbtCompound();

				type.serializer().writeNbt(temp, value);
				Object copy = type.serializer().readNbt(temp);

				newContainer.setRaw(type, copy);
                ((EntityMixinAccessor) newPlayer).hatred$syncFull(newPlayer);
            }
		});

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> Data.API.get(entity, ENTITY_SCHEDULER).clear());
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> entity.cancelAll(false));

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registries.ITEM, new Identifier(MOD_ID, "test"), new TestItem(new FabricItemSettings()));
		}
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}