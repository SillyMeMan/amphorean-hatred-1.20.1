package net.vinh.hatred.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.misc.AbstractBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("all")
public record DamageContext(RegistryEntry<DamageType> type, @Nullable Text deathMessage, @Nullable Entity attacker, @Nullable Entity directSource, float rawDamage, boolean bypassesArmor, boolean bypassesResistance, boolean bypassesEnchantments, boolean bypassesInvulnerability, boolean bypassesTotems, @Nullable List<StatusEffectInstance> hitEffects, @Nullable Vec3d knockback, boolean nonFatal, boolean trueDamage) {}
