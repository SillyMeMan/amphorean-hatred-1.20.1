package net.vinh.hatred.api.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.vinh.hatred.api.util.Args;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.ability.AbilityTimeline;
import net.vinh.hatred.internal.ability.AbstractAbility;
import org.jetbrains.annotations.Nullable;

public class Ability extends AbstractAbility {
    private final int cooldown;
    private final int interruptCooldown;
    private final long duration;

    private @Nullable String translationKey;

    public Ability(int cooldown, int interruptCooldown, long duration) {
        if(cooldown < 0 || interruptCooldown < 0 || duration < 0) throw new IllegalArgumentException("No constructor arguments can be negative");

        this.cooldown = cooldown;
        this.interruptCooldown = interruptCooldown;
        this.duration = duration;
    }

    @Override
    public Args preCast(LivingEntity caster) {
        return Args.ofImmutable();
    }

    @Override
    public void cast(AbilityTimeline timeline, LivingEntity caster, Args args) {

    }

    @Override
    public final int cooldown() {
        return cooldown;
    }

    @Override
    public final int interruptCooldown() {
        return interruptCooldown;
    }

    @Override
    public final long duration() {
        return duration;
    }

    public Text getName() {
        return Text.translatable(this.getTranslationKey());
    }

    @Override
    public String toString() {
        return "Ability (" + this.getName() + ")";
    }

    protected String getOrCreateTranslationKey() {
        if(this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("ability", HatredRegistries.ABILITY.getId(this));
        }

        return this.translationKey;
    }

    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }
}
