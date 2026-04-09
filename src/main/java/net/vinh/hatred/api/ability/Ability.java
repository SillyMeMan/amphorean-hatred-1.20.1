package net.vinh.hatred.api.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.internal.ability.AbstractAbility;
import org.jetbrains.annotations.Nullable;

public class Ability extends AbstractAbility {
    private final int preCastTime;
    private final int cooldown;
    private final int interruptCooldown;
    private final int maxCharges;

    private @Nullable String translationKey;

    public Ability(int preCastTime, int cooldown, int interruptCooldown, int maxCharges) {
        this.preCastTime = preCastTime;
        this.cooldown = cooldown;
        this.interruptCooldown = interruptCooldown;
        this.maxCharges = maxCharges;
    }

    @Override
    public void preCast(LivingEntity caster) {
        // what the hell
    }

    @Override
    public void cast(LivingEntity caster) {
        // what the fuck
    }

    @Override
    public int preCastTime() {
        return preCastTime;
    }

    @Override
    public int cooldown() {
        return cooldown;
    }

    @Override
    public int interruptCooldown() {
        return interruptCooldown;
    }

    @Override
    public int maxCharges() {
        return maxCharges;
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
