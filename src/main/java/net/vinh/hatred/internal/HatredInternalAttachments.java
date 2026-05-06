package net.vinh.hatred.internal;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.ability.CooldownEntry;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataRegistry;
import net.vinh.hatred.api.data.DataSerializers;
import net.vinh.hatred.internal.scheduler.EntityScheduler;
import net.vinh.hatred.internal.scheduler.WorldScheduler;
import net.vinh.hatred.internal.ability.AbstractAbility;

import java.util.HashMap;
import java.util.Map;

import static net.vinh.hatred.AmphoreanHatred.id;

public class HatredInternalAttachments {
    public static final DataAttachmentType<WorldScheduler> WORLD_SCHEDULER = DataRegistry.register(AmphoreanHatred.id("world_scheduler"), WorldScheduler.class, WorldScheduler::new, null, false, false);
    public static final DataAttachmentType<EntityScheduler> ENTITY_SCHEDULER = DataRegistry.register(AmphoreanHatred.id("entity_scheduler"), EntityScheduler.class, EntityScheduler::new, null, false, false);

    public static final DataAttachmentType<Boolean> IS_USING_ABILITY = DataRegistry.registerBoolean(AmphoreanHatred.id("is_using_ability"), () -> false, false, true);
    public static final DataAttachmentType<Boolean> NO_STUN = DataRegistry.registerBoolean(AmphoreanHatred.id("no_stun"), () -> false, false, true);

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
