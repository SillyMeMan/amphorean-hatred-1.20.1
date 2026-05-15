package net.vinh.hatred.api.ability.state;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.ColorHelper;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.misc.UnprotectedStatusEffect;
import net.vinh.hatred.api.registry.IAutoRegisterable;
import net.vinh.hatred.util.Utils;

public class CombatStates implements IAutoRegisterable {
    public static final StatusEffect MOVEMENT_FREEZE = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("movement_freeze"), new UnprotectedStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));
    public static final StatusEffect INVENTORY_FREEZE = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("inventory_freeze"), new UnprotectedStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));
    public static final StatusEffect ROTATION_LOCK = Utils.RegistryHelper.statusEffect(AmphoreanHatred.id("rotation_lock"), new UnprotectedStatusEffect(StatusEffectCategory.HARMFUL, ColorHelper.Argb.getArgb(1, 255, 0, 0)));

    @Override
    public void staticInit() {

    }
}
