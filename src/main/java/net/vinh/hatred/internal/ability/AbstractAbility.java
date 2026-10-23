package net.vinh.hatred.internal.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.util.Args;

public abstract class AbstractAbility {
    public abstract Args preCast(LivingEntity caster);

    public abstract void cast(AbilityTimeline timeline, LivingEntity caster, Args args);

    /**
     * Can be used to specify a cooldown group.
     * <p>
     * Any ability in the same cooldown group goes on cooldown if
     * any one of the ability in the cooldown group gets put on cooldown
     * @return An identifier of the cooldown group (currently not implemented)
     */
    public Identifier cooldownGroup() {
        return null;
    }

    public void onCancelled(LivingEntity caster) {

    }

    public void onFinished(LivingEntity caster) {

    }

    public abstract int cooldown();
    public abstract int interruptCooldown();
    public abstract long duration();
}
