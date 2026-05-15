package net.vinh.hatred.api.misc;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class UnprotectedStatusEffect extends StatusEffect {
    public UnprotectedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}
