package net.vinh.hatred.api.damage.element;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;

import java.util.List;

import static net.vinh.hatred.AmphoreanHatred.id;

/**
 * This elemental type system is supposed to be used in tandem with the {@link net.vinh.hatred.api.damage.DamageContext DamageContext} system
 * using the {@link net.vinh.hatred.api.damage.DamageContext#deathMessage() deathMessage} setting. Default death messages are available for all the elemental types
 */
public class ElementalTypes {
    public static final RegistryKey<DamageType> PHYSICAL = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("physical"));
    public static final RegistryKey<DamageType> FIRE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("fire"));
    public static final RegistryKey<DamageType> QUANTUM = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("quantum"));
    public static final RegistryKey<DamageType> IMAGINARY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("imaginary"));
    public static final RegistryKey<DamageType> LIGHTNING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("lightning"));
    public static final RegistryKey<DamageType> WIND = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("wind"));
    public static final RegistryKey<DamageType> ICE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("ice"));

    public static final ImmutableList<RegistryKey<DamageType>> TYPES = ImmutableList.of(PHYSICAL, FIRE, QUANTUM, IMAGINARY, LIGHTNING, WIND, ICE);

    public static RegistryKey<DamageType> getRandomKey() {
        return Util.getRandom(TYPES, Random.createLocal());
    }
}
