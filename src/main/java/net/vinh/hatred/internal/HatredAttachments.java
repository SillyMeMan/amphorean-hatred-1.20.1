package net.vinh.hatred.internal;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.ability.CooldownEntry;
import net.vinh.hatred.api.collision.Barrier;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataRegistry;
import net.vinh.hatred.api.data.DataSerializers;
import net.vinh.hatred.api.scheduler.EntityScheduler;
import net.vinh.hatred.api.scheduler.WorldScheduler;
import net.vinh.hatred.internal.ability.AbstractAbility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.vinh.hatred.AmphoreanHatred.id;

public class HatredAttachments {
    public static final DataAttachmentType<Boolean> MOVEMENT_FROZEN = DataRegistry.registerBoolean(AmphoreanHatred.id("movement_frozen"), () -> false, false, true);
    public static final DataAttachmentType<Boolean> INVENTORY_FROZEN = DataRegistry.registerBoolean(AmphoreanHatred.id("inventory_frozen"), () -> false, false, true);
    public static final DataAttachmentType<Boolean> ROTATION_LOCKED = DataRegistry.registerBoolean(AmphoreanHatred.id("rotation_locked"), () -> false, false, true);

    public static final DataAttachmentType<WorldScheduler> WORLD_SCHEDULER = DataRegistry.register(new Identifier(AmphoreanHatred.MOD_ID, "world_scheduler"), WorldScheduler.class, WorldScheduler::new, null, false, false);
    public static final DataAttachmentType<EntityScheduler> ENTITY_SCHEDULER = DataRegistry.register(new Identifier(AmphoreanHatred.MOD_ID, "entity_scheduler"), EntityScheduler.class, EntityScheduler::new, null, false, false);

    public static final DataAttachmentType<Map<Identifier, CooldownEntry>> ABILITY_COOLDOWNS =
            DataRegistry.register(
                    id("ability_cooldowns"),
                    (Class<Map<Identifier, CooldownEntry>>)(Class<?>) Map.class,
                    HashMap::new,
                    DataSerializers.of(
                            (nbt, map) -> {
                                for (var entry : map.entrySet()) {

                                    NbtCompound sub = new NbtCompound();

                                    CooldownEntry cd = entry.getValue();

                                    sub.putLong("ready", cd.readyTick);
                                    sub.putInt("charges", cd.charges);
                                    sub.putInt("max", cd.maxCharges);

                                    nbt.put(entry.getKey().toString(), sub);
                                }
                            },
                            nbt -> {
                                Map<Identifier, CooldownEntry> map = new HashMap<>();

                                for (String key : nbt.getKeys()) {

                                    NbtCompound sub = nbt.getCompound(key);

                                    map.put(
                                            new Identifier(key),
                                            new CooldownEntry(
                                                    sub.getLong("ready"),
                                                    sub.getInt("charges"),
                                                    sub.getInt("max")
                                            )
                                    );
                                }

                                return map;
                            }
                    ),
                    true,
                    true
            );

    public static final DataAttachmentType<Map<Identifier, AbstractAbility.PreCastInstance>> PRECASTS =
            DataRegistry.register(
                    id("precasts"),
                    (Class<Map<Identifier, AbstractAbility.PreCastInstance>>)(Class<?>)Map.class,
                    HashMap::new,
                    DataSerializers.of(
                            (nbt, map) -> {
                                for (Map.Entry<Identifier, AbstractAbility.PreCastInstance> entry : map.entrySet()) {

                                    AbstractAbility.PreCastInstance instance = entry.getValue();

                                    NbtCompound sub = new NbtCompound();

                                    sub.putString("ability", instance.abilityId.toString());
                                    sub.putLong("start", instance.startTick);
                                    sub.putLong("cast", instance.castTick);
                                    sub.putBoolean("cancelled", instance.cancelled);

                                    nbt.put(entry.getKey().toString(), sub);
                                }
                            },
                            nbt -> {
                                Map<Identifier, AbstractAbility.PreCastInstance> map = new HashMap<>();

                                for (String key : nbt.getKeys()) {

                                    Identifier id = new Identifier(key);

                                    NbtCompound sub = nbt.getCompound(key);

                                    Identifier ability =
                                            new Identifier(sub.getString("ability"));

                                    long start = sub.getLong("start");
                                    long cast = sub.getLong("cast");

                                    AbstractAbility.PreCastInstance instance =
                                            new AbstractAbility.PreCastInstance(ability, start, cast);

                                    instance.cancelled = sub.getBoolean("cancelled");

                                    map.put(id, instance);
                                }

                                return map;
                            }
                    ),
                    false,
                    true
            );
}
