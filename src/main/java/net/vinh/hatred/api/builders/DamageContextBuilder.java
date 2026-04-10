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
    private boolean bypassesArmor = false;
    private boolean bypassesResistance = false;
    private boolean bypassesEnchantments = false;
    private boolean bypassesInvulnerability = false;
    private boolean bypassesTotems = false;
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

    public DamageContextBuilder nonFatal() {
        this.nonFatal = true;
        return this;
    }

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
        return new DamageContext(type, deathMessage, attacker, directSource, bypassesArmor, bypassesResistance, bypassesEnchantments, bypassesInvulnerability, bypassesTotems, nonFatal, trueDamage, addKilledDisplayNameToMsg, hitEffects, knockback);
    }
}