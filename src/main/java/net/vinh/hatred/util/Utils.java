package net.vinh.hatred.util;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.ability.IAbility;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.api.builders.HudTextBuilder;
import net.vinh.hatred.api.event.ServerAbilityEvents;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.exception.InvalidAbilityNumberException;
import net.vinh.hatred.internal.util.ServerCrashHandler;
import net.vinh.hatred.networking.packet.CrashS2CPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class Utils {
    private Utils() {
        throw new AssertionError("Not supposed to be instantized!");
    }

    public static class RegistryHelper {
        private RegistryHelper() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static <T extends Ability> T ability(Identifier id, T ability) {
            return Registry.register(HatredRegistries.ABILITY, id, ability);
        }

        public static <T extends Item> T item(Identifier id, T item) {
            return Registry.register(Registries.ITEM, id, item);
        }

        public static <T extends Block> T blockWithItem(Identifier id, T block, @Nullable Item.Settings settings) {
            Registry.register(Registries.ITEM, id, new BlockItem(block, settings != null ? settings : new FabricItemSettings()));
            return block(id, block);
        }

        public static <T extends Block> T block(Identifier id, T block) {
            return Registry.register(Registries.BLOCK, id, block);
        }

        public static <T extends BlockEntity> BlockEntityType<T> blockEntity(Identifier id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
            return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(factory, blocks).build());
        }

        public static <T extends Entity> EntityType<T> entity(Identifier id, FabricEntityTypeBuilder<T> builder) {
            return Registry.register(Registries.ENTITY_TYPE, id, builder.build());
        }

        public static <T extends StatusEffect> T statusEffect(Identifier id, T effect) {
            return Registry.register(Registries.STATUS_EFFECT, id, effect);
        }

        public static <T extends Potion> T potion(Identifier id, T potion) {
            return Registry.register(Registries.POTION, id, potion);
        }

        public static <T extends ParticleType<?>> T particle(Identifier id, T particle) {
            return Registry.register(Registries.PARTICLE_TYPE, id, particle);
        }

        public static <T extends RecipeSerializer<?>> T recipeSerializer(Identifier id, T serializer) {
            return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
        }

        public static <T extends Recipe<?>> RecipeType<T> recipeType(Identifier id) {
            return Registry.register(Registries.RECIPE_TYPE, id, new RecipeType<>() {
                public String toString() { return id.toString(); }
            });
        }

        public static <T extends Enchantment> T enchantment(Identifier id, T enchantment) {
            return Registry.register(Registries.ENCHANTMENT, id, enchantment);
        }

        public static <T extends FeatureConfig> Feature<T> feature(Identifier id, Feature<T> feature) {
            return Registry.register(Registries.FEATURE, id, feature);
        }

        public static <T extends ScreenHandler> ScreenHandlerType<T> screenHandler(Identifier id, ScreenHandlerType.Factory<T> factory, FeatureSet requiredFeatures) {
            return Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory, requiredFeatures));
        }

        public static SoundEvent soundEvent(Identifier id) {
            return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
        }
    }

    public static class Conversion {
        private Conversion() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static <T> RegistryEntry<T> getEntryFromKey(World world, RegistryKey<Registry<T>> sourceKey, RegistryKey<T> key) {
            return world.getRegistryManager().get(sourceKey).entryOf(key);
        }

        public static RegistryEntry<DamageType> getEntryFromKey(World world, RegistryKey<DamageType> key) {
            return getEntryFromKey(world, RegistryKeys.DAMAGE_TYPE, key);
        }
    }

    public static class Server {
        private Server() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static void stopServer(MinecraftServer server) {
            server.stop(false);
        }

        public static void ban(@NotNull ServerPlayerEntity player, Text reason) {
            assert player.getServer() != null;
            BannedPlayerList list = player.getServer().getPlayerManager().getUserBanList();

            if(!list.contains(player.getGameProfile())) {
                BannedPlayerEntry entry = new BannedPlayerEntry(player.getGameProfile(), null, "Server", null, reason.getString());
                list.add(entry);
                player.networkHandler.disconnect(reason);
            } else {
                AmphoreanHatred.LOGGER.warn("The target player has already been banned");
            }
        }

        public static void banIp(ServerPlayerEntity player, Text reason) {
            assert player.getServer() != null;
            BannedIpList bannedIpList = player.getServer().getPlayerManager().getIpBanList();
            if (bannedIpList.isBanned(player.getIp())) {
                AmphoreanHatred.LOGGER.warn("The target IP has already been banned");
            } else {
                List<ServerPlayerEntity> list = player.getServer().getPlayerManager().getPlayersByIp(player.getIp());
                BannedIpEntry bannedIpEntry = new BannedIpEntry(player.getIp(), null, "Server", null, reason.getString());
                bannedIpList.add(bannedIpEntry);

                for(ServerPlayerEntity serverPlayerEntity : list) {
                    serverPlayerEntity.networkHandler.disconnect(reason);
                }
            }
        }
    }

    /**
     * Welcome to the forbidden zone of this class. Proceed with caution. Serious damage can be caused if used randomly, and you will regret doing that.
     */
    public static class ForbiddenZone {
        private ForbiddenZone() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static void endSomeone(ServerPlayerEntity target, boolean hardCrash, String reason) {
            PacketByteBuf buf = PacketByteBufs.create();

            (new CrashS2CPacket(hardCrash, reason)).write(buf);

            ServerPlayNetworking.send(target, CrashS2CPacket.ID, buf);
        }

        public static void endServer(MinecraftServer server, boolean hardCrash, String reason) {
            if (hardCrash) {
                System.exit(1);
            } else {
                server.execute(() -> {
                    ServerCrashHandler.shouldCrash = true;
                    ServerCrashHandler.reason = reason;
                });
            }
        }
    }

    public static class Builders {
        private Builders() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static DamageContextBuilder contextBuilder() {
            return new DamageContextBuilder();
        }

        public static HudTextBuilder textBuilder() {
            return new HudTextBuilder();
        }
    }

    @java.lang.Deprecated
    public static class Deprecated {
        private Deprecated() {
            throw new AssertionError("Not supposed to be instantized!");
        }

        public static void useAbility(ServerPlayerEntity player, int abilityNumber) {
            if(!ServerAbilityEvents.USE_ABILITY.invoker().useAbility(player, abilityNumber)) {
                return;
            }

            Item item = player.getMainHandStack().getItem();

            if(item instanceof IAbility abilityItem) {
                switch (abilityNumber) {
                    case 1 -> {
                        if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 1)) {
                            abilityItem.alt_ability_1(player);
                        }
                    }
                    case 2 -> {
                        if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 2)) {
                            abilityItem.alt_ability_2(player);
                        }
                    }
                    case 3 -> {
                        if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 3)) {
                            abilityItem.alt_ability_3(player);
                        }
                    }
                    case 4 -> {
                        if(!IAbility.AbilityCooldownManager.isOnCooldown(player.getMainHandStack(), 4)) {
                            abilityItem.alt_ability_4(player);
                        }
                    }
                    default -> throw new InvalidAbilityNumberException();
                }
            }
        }
    }
}