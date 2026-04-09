package net.vinh.hatred.api.builders;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.api.damage.DamageContext;
import net.vinh.hatred.api.misc.AbstractBuilder;

import java.util.List;

public class DamageContextBuilder extends AbstractBuilder<DamageContext> {
    private RegistryEntry<DamageType> type;

    private Text deathMessage;
    private Entity attacker;
    private Entity directSource;
    private float rawDamage;
    private boolean bypassesArmor;
    private boolean bypassesResistance;
    private boolean bypassesEnchantments;
    private boolean bypassesInvulnerability;
    private boolean bypassesTotems;
    private List<StatusEffectInstance> hitEffects;
    private Vec3d knockback;
    private boolean nonFatal;
    private boolean trueDamage;

    public DamageContextBuilder type(RegistryEntry<DamageType> type) {
        this.type = type;
        return this;
    }

    public DamageContextBuilder deathMessage(Text deathMessage) {
        this.deathMessage = deathMessage;
        return this;
    }

    public DamageContextBuilder attacker(Entity attacker) {
        this.attacker = attacker;
        return this;
    }

    public DamageContextBuilder directSource(Entity directSource) {
        this.directSource = directSource;
        return this;
    }

    public DamageContextBuilder rawDamage(float rawDamage) {
        this.rawDamage = rawDamage;
        return this;
    }

    public DamageContextBuilder bypassesArmor() {
        this.bypassesArmor = true;
        return this;
    }

    public DamageContextBuilder bypassesResistance() {
        this.bypassesResistance = true;
        return this;
    }

    public DamageContextBuilder bypassesEnchantments() {
        this.bypassesEnchantments = true;
        return this;
    }

    public DamageContextBuilder bypassesInvulnerability() {
        this.bypassesInvulnerability = true;
        return this;
    }

    public DamageContextBuilder bypassesTotems() {
        this.bypassesTotems = true;
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

    public DamageContextBuilder nonFatal() {
        this.nonFatal = true;
        return this;
    }

    public DamageContextBuilder trueDamage() {
        this.trueDamage = true;
        return this;
    }

    @Override
    public DamageContext build() {
        return new DamageContext(type, deathMessage, attacker, directSource, rawDamage, bypassesArmor, bypassesResistance, bypassesEnchantments, bypassesInvulnerability, bypassesTotems, hitEffects, knockback, nonFatal, trueDamage);
    }
}