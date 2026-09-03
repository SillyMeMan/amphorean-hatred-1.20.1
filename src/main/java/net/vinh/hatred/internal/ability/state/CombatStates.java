package net.vinh.hatred.internal.ability.state;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.ColorHelper;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.misc.EmptyStatusEffect;
import net.vinh.hatred.api.registry.AutoInitializer;
import net.vinh.hatred.util.Utils;

@AutoInitializer
public class CombatStates {
    public static final StatusEffect MOVEMENT_FREEZE = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("movement_freeze"), new EmptyStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));
    public static final StatusEffect INVENTORY_FREEZE = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("inventory_freeze"), new EmptyStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));
    public static final StatusEffect ROTATION_LOCK = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("rotation_lock"), new EmptyStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));
}
