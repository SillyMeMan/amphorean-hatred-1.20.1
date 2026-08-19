package net.vinh.hatred.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DamageContext(RegistryEntry<DamageType> type, @Nullable Text deathMessage, @Nullable Entity attacker, @Nullable Entity directSource, float armorEffectivenessMultiplier, boolean bypassesArmor, boolean bypassesAbsorption, boolean bypassesShield, boolean bypassesResistance, boolean bypassesEnchantments, boolean bypassesInvulnerability, boolean bypassesTotems, boolean bypassesCooldown, boolean alwaysDamageEnderDragons, boolean nonFatal, boolean trueDamage, boolean addKilledDisplayNameToMsg, @Nullable List<StatusEffectInstance> hitEffects, @Nullable Vec3d knockback) {}
