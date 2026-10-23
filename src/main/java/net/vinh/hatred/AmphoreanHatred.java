package net.vinh.hatred;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.brigadier.arguments.AbilityArgumentType;
import net.vinh.hatred.api.command.AbilityCommand;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.cca.HatredLivingEntityComponent;
import net.vinh.hatred.cca.ModComponents;
import net.vinh.hatred.internal.ability.AbilityManager;
import net.vinh.hatred.impl.TestItem;
import net.vinh.hatred.internal.AutoRegistry;
import net.vinh.hatred.internal.scheduler.Schedulers;
import net.vinh.hatred.util.C2SPacketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmphoreanHatred implements ModInitializer {
	public static final String MOD_ID = "hatred";
	public static final Logger LOGGER = LoggerFactory.getLogger("Amphorean Hatred");

	@Override
	public void onInitialize() {
		HatredRegistries.initRegistries();
        AutoRegistry.autoCommonBootstrap();
        AbilityArgumentType.init();
		AbilityManager.initialize();

		CommandRegistrationCallback.EVENT.register(AbilityCommand::register);

		C2SPacketHelper.initializeServer();

		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if(entity instanceof LivingEntity living) {
				Schedulers.entity(living).clear();
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			// just to prevent anyone from cheating the system
			for(Identifier abilityId : HatredRegistries.ABILITY.getIds()) {
				player.setCooldown(HatredRegistries.ABILITY.get(abilityId), HatredRegistries.ABILITY.get(abilityId).cooldown());
			}

			HatredLivingEntityComponent component = ModComponents.LIVING_ENTITY.get(player);

			component.movementFrozen = false;
			component.rotationLocked = false;

			component.isInvulnerable = false;
			component.isPureInvulnerable = false;

			player.syncComponent(ModComponents.LIVING_ENTITY);
		});

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> Schedulers.entity(entity).clear());
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> entity.cancelAll(true));

		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registries.ITEM, id("test"), new TestItem(new FabricItemSettings()));
		}
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}