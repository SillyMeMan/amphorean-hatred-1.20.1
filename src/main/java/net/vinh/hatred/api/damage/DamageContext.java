package net.vinh.hatred.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DamageContext(DamageSource source, @Nullable Entity attacker, @Nullable Entity directSource, float rawDamage, boolean bypassArmor, boolean bypassResistance, boolean bypassInvulnerability, boolean bypassTotems, @Nullable List<StatusEffectInstance> hitEffects, @Nullable Vec3d knockback) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DamageSource source;
        private Entity attacker;
        private Entity directSource;
        private float rawDamage;
        private boolean bypassesArmor;
        private boolean bypassesResistance;
        private boolean bypassesInvulnerability;
        private boolean bypassesTotems;
        private List<StatusEffectInstance> hitEffects;
        private Vec3d knockback;


        public Builder source(DamageSource source) {
            this.source = source;
            return this;
        }

        public Builder attacker(Entity attacker) {
            this.attacker = attacker;
            return this;
        }

        public Builder directSource(Entity directSource) {
            this.directSource = directSource;
            return this;
        }

        public Builder rawDamage(float rawDamage) {
            this.rawDamage = rawDamage;
            return this;
        }

        public Builder bypassesArmor() {
            this.bypassesArmor = true;
            return this;
        }

        public Builder bypassesResistance() {
            this.bypassesResistance = true;
            return this;
        }

        public Builder bypassesInvulnerability() {
            this.bypassesInvulnerability = true;
            return this;
        }

        public Builder bypassesTotems() {
            this.bypassesTotems = true;
            return this;
        }

        public Builder hitEffects(List<StatusEffectInstance> effects) {
            this.hitEffects = effects;
            return this;
        }

        public Builder knockback(Vec3d knockback) {
            this.knockback = knockback;
            return this;
        }

        public DamageContext build() {
            return new DamageContext(source, attacker, directSource, rawDamage, bypassesArmor, bypassesResistance, bypassesInvulnerability, bypassesTotems, hitEffects, knockback);
        }
    }
}
