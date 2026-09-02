package net.vinh.hatred.api.builders;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.misc.AbstractBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DamageContextBuilder extends AbstractBuilder<DamageContext> {
    private RegistryEntry<DamageType> type;

    @Nullable private Text deathMessage = null;
    @Nullable private Entity attacker;
    @Nullable private Entity directSource;
    private float armorEffectivenessMultiplier = 1.0f;
    private boolean bypassesArmor = false;
    private boolean bypassesAbsorption = false;
    private boolean bypassesShield = false;
    private boolean bypassesResistance = false;
    private boolean bypassesEnchantments = false;
    private boolean bypassesInvulnerability = false;
    private boolean bypassesTotems = false;
    private boolean bypassesCooldown = false;
    private boolean alwaysDamageEnderDragons = false;
    private boolean neverDamageEnderDragons = false;
    private boolean nonFatal = false;
    private boolean trueDamage = false;
    private boolean addKilledDisplayNameToMsg = false;
    @Nullable private List<StatusEffectInstance> hitEffects = null;
    @Nullable private Vec3d knockback = null;

    public DamageContextBuilder type(RegistryEntry<DamageType> type) {
        this.type = type;
        return this;
    }

    public DamageContextBuilder deathMessage(Text deathMessage) {
        this.deathMessage = deathMessage;
        return this;
    }

    public DamageContextBuilder attacker(@Nullable Entity attacker) {
        this.attacker = attacker;
        return this;
    }

    public DamageContextBuilder directSource(@Nullable Entity directSource) {
        this.directSource = directSource;
        return this;
    }

    /**
     * Returns the multiplier applied to the target's armor effectiveness.
     *
     * <p>A value of {@code 1.0} represents normal armor effectiveness.
     * Values below {@code 1.0} reduce armor effectiveness, while values
     * above {@code 1.0} increase it.
     *
     * <p>If {@link #bypassesArmor()} is enabled, this multiplier is ignored.
     *
     * @return the armor effectiveness multiplier
     */
    public DamageContextBuilder armorEffectivenessMultiplier(float armorEffectivenessMultiplier) {
        this.armorEffectivenessMultiplier = armorEffectivenessMultiplier;
        return this;
    }

    /**
     * Causes this damage to completely bypass the target's armor.
     *
     * <p>This takes precedence over {@link #armorEffectivenessMultiplier(float)}.
     * When armor is bypassed, the armor effectiveness multiplier is ignored during damage calculation.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesArmor() {
        this.bypassesArmor = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the target's absorption.
     * <p>
     * When absorption is bypassed, this damage immediately goes to HP, completely ignoring absorption.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesAbsorption() {
        this.bypassesAbsorption = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the target's shield.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesShield() {
        this.bypassesShield = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the target's Resistance status effect.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesResistance() {
        this.bypassesResistance = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the target's enchantments. This mostly includes the {@linkplain net.minecraft.enchantment.ProtectionEnchantment Protection} enchantment and their variants
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesEnchantments() {
        this.bypassesEnchantments = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass invulnerability. This includes the invulnerability gained from Creative mode.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesInvulnerability() {
        this.bypassesInvulnerability = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the effects of Totems of Undying and any death protection items hooked into tryUseTotem() through Mixins.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesTotems() {
        this.bypassesTotems = true;
        return this;
    }

    /**
     * Causes this damage to completely bypass the IFrames gained from taking damage.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder bypassesCooldown() {
        this.bypassesCooldown = true;
        return this;
    }

    /**
     * Causes this damage to always take effect on {@link net.minecraft.entity.boss.dragon.EnderDragonEntity}.
     * If both this and {@link #neverDamageEnderDragons()} is enabled, it will default to if the damage type has the {@link net.minecraft.registry.tag.DamageTypeTags#ALWAYS_HURTS_ENDER_DRAGONS} tag or not
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder alwaysDamageEnderDragons() {
        this.alwaysDamageEnderDragons = true;
        return this;
    }

    /**
     * Causes this damage to never take effect on {@link net.minecraft.entity.boss.dragon.EnderDragonEntity}.
     * If both this and {@link #alwaysDamageEnderDragons()} is enabled, it will default to if the damage type has the {@link net.minecraft.registry.tag.DamageTypeTags#ALWAYS_HURTS_ENDER_DRAGONS} tag or not
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder neverDamageEnderDragons() {
        this.neverDamageEnderDragons = true;
        return this;
    }

    /**
     * Causes this damage to be non-fatal. If under normal circumstances this damage is fatal and the target has no Totems of Undying or {@link #bypassesTotems()} is in effect, the target will only take enough damage to have 1 health, any remaining damage is ignored
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder nonFatal() {
        this.nonFatal = true;
        return this;
    }

    /**
     * Causes this damage to bypass most core systems of Minecraft's damage API and instantly set the health of the target through {@link net.minecraft.entity.LivingEntity#setHealth(float)}. This also bypasses attack dependant enchantments such as {@linkplain net.minecraft.enchantment.ThornsEnchantment Thorns}.
     *
     * @return This instance of the builder for convenience in chaining options
     */
    public DamageContextBuilder trueDamage() {
        this.trueDamage = true;
        return this;
    }

    public DamageContextBuilder addKilledDisplayNameToMsg() {
        this.addKilledDisplayNameToMsg = true;
        return this;
    }

    public DamageContextBuilder hitEffects(List<StatusEffectInstance> effects) {
        this.hitEffects = effects;
        return this;
    }

    public DamageContextBuilder knockback(Vec3d knockback) {
        this.knockback = knockback;
        return this;
    }

    @Override
    public DamageContext build() {
        return new DamageContext(type, deathMessage, attacker, directSource, armorEffectivenessMultiplier, bypassesArmor, bypassesAbsorption, bypassesShield, bypassesResistance, bypassesEnchantments, bypassesInvulnerability, bypassesTotems, bypassesCooldown, alwaysDamageEnderDragons, neverDamageEnderDragons, nonFatal, trueDamage, addKilledDisplayNameToMsg, hitEffects, knockback);
    }
}