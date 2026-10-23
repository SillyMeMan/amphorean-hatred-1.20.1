package net.vinh.hatred.internal.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.util.Args;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class AbilityExecution implements AbilityTimeline {
    private final LivingEntity caster;
    private final Ability ability;
    private final Identifier abilityId;
    private final Args args;

    private final List<Keyframe> keyframes = new ArrayList<>();

    private boolean finalized;

    private long elapsedTicks;
    private boolean cancelled;
    private boolean finished;
    private boolean cancellationCallbackRun;

    public AbilityExecution(
            LivingEntity caster,
            Ability ability,
            Identifier abilityId,
            Args args
    ) {
        this.caster = caster;
        this.ability = ability;
        this.abilityId = abilityId;
        this.args = args.toImmutable();
    }

    public void initialize() {
        ability.cast(this, this.caster, args);
        keyframes.sort(Comparator.comparingLong(Keyframe::tick));
    }

    @Override
    public void atTick(long tick, Runnable action) {
        if (tick < 0) {
            throw new IllegalArgumentException("Keyframe tick cannot be negative");
        }

        if(tick > ability.duration()) {
            throw new IllegalArgumentException("Keyframe tick cannot be higher than the ability duration");
        }

        if (action == null) {
            throw new IllegalArgumentException("Keyframe action cannot be null");
        }

        keyframes.add(new Keyframe(tick, action));
    }

    public void tick() {
        if (cancelled || finished) {
            return;
        }

        for (Keyframe keyframe : keyframes) {
            if (cancelled || finished) {
                return;
            }

            if (!keyframe.executed() && keyframe.tick() <= elapsedTicks) {
                keyframe.run();
            }
        }

        if (cancelled || finished) {
            return;
        }

        if (elapsedTicks >= ability.duration()) {
            finished = true;
            return;
        }

        elapsedTicks++;
    }

    public void cancel(boolean triggerCallback) {
        if (cancelled || finished) {
            return;
        }

        cancelled = true;

        if (triggerCallback && !cancellationCallbackRun) {
            cancellationCallbackRun = true;
            ability.onCancelled(caster);
        }
    }

    public LivingEntity caster() {
        return caster;
    }

    public Ability ability() {
        return ability;
    }

    public Identifier abilityId() {
        return abilityId;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean markFinalized() {
        if (finalized) {
            return false;
        }

        finalized = true;
        return true;
    }

    private static final class Keyframe {
        private final long tick;
        private final Runnable action;
        private boolean executed;

        private Keyframe(long tick, Runnable action) {
            this.tick = tick;
            this.action = action;
        }

        public long tick() {
            return tick;
        }

        public boolean executed() {
            return executed;
        }

        public void run() {
            executed = true;
            action.run();
        }
    }
}

