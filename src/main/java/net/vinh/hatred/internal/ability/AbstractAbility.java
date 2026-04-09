package net.vinh.hatred.internal.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class AbstractAbility {
    /**
     * Anything inside this void method instantly runs upon ability usage
     */
    public abstract void preCast(LivingEntity caster);

    /**
     * Anything inside this void method runs when the countdown/pre-cast state ends.
     */
    public abstract void cast(LivingEntity caster);

    /**
     *    Can be used to specify a cooldown group.
     * <p>
     *    Any ability in the same cooldown group goes on cooldown if
     * any one of the ability in the cooldown group gets put on cooldown
     * @return An identifier of the cooldown group (currently not implemented)
     */
    public Identifier cooldownGroup() {
        return null;
    }

    public abstract int preCastTime();

    public abstract int cooldown();

    public abstract int interruptCooldown();

    public abstract int maxCharges();

    public static class PreCastInstance {
        public final Identifier abilityId;

        public final long startTick;
        public final long castTick;

        public boolean cancelled;

        public PreCastInstance(Identifier abilityId, long startTick, long castTick) {
            this.abilityId = abilityId;
            this.startTick = startTick;
            this.castTick = castTick;
        }
    }
}
