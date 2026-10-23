package net.vinh.hatred.internal.ability;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.ability.Cooldowns;
import net.vinh.hatred.cca.ModComponents;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class AbilityManager {
    private static final Map<MinecraftServer, AbilityManager> MANAGERS =
            new IdentityHashMap<>();

    private final List<AbilityExecution> executions = new ArrayList<>();

    private AbilityManager(MinecraftServer server) {}

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server ->
                get(server).tick()
        );

        ServerLifecycleEvents.SERVER_STOPPED.register(MANAGERS::remove);
    }

    public static AbilityManager get(MinecraftServer server) {
        return MANAGERS.computeIfAbsent(
                server,
                AbilityManager::new
        );
    }

    public boolean hasExecution(LivingEntity caster, Identifier abilityId) {
        return executions.stream().anyMatch(execution ->
                execution.caster() == caster
                        && execution.abilityId().equals(abilityId)
                        && !execution.isCancelled()
                        && !execution.isFinished()
        );
    }

    public void start(AbilityExecution execution) {
        executions.add(execution);
    }

    public void cancel(
            LivingEntity caster,
            Identifier abilityId,
            boolean triggerCallback
    ) {
        for (AbilityExecution execution : List.copyOf(executions)) {
            if (execution.caster() == caster
                    && execution.abilityId().equals(abilityId)) {
                execution.cancel(triggerCallback);
            }
        }
    }

    public void cancelAll(
            LivingEntity caster,
            boolean triggerCallback
    ) {
        for (AbilityExecution execution : List.copyOf(executions)) {
            if (execution.caster() == caster) {
                execution.cancel(triggerCallback);
            }
        }
    }

    public boolean hasActiveExecution(LivingEntity caster) {
        return executions.stream().anyMatch(execution ->
                execution.caster() == caster
                        && !execution.isCancelled()
                        && !execution.isFinished()
        );
    }

    private void tick() {
        for (AbilityExecution execution : List.copyOf(executions)) {
            if (!execution.isCancelled() && !execution.isFinished()) {
                execution.tick();
            }

            if (execution.isCancelled() || execution.isFinished()) {
                finalizeExecution(execution);
            }
        }

        executions.removeIf(execution ->
                execution.isCancelled() || execution.isFinished());
    }

    private void finalizeExecution(AbilityExecution execution) {
        if (!execution.isCancelled() && !execution.isFinished()) {
            return;
        }

        if (!execution.markFinalized()) {
            return;
        }

        LivingEntity caster = execution.caster();

        try {
            long cooldown = execution.isCancelled()
                    ? execution.ability().interruptCooldown()
                    : execution.ability().cooldown();

            if(execution.isFinished()) {
                execution.ability().onFinished(caster);
            }

            if (caster.getServer() != null) {
                Cooldowns.setCooldown(
                        caster,
                        execution.abilityId(),
                        cooldown
                );
            }
        } finally {
            if (caster.getServer() != null) {
                ModComponents.LIVING_ENTITY.get(caster).isUsingAbility = hasActiveExecution(caster);

                ModComponents.LIVING_ENTITY.sync(caster);
            }
        }
    }
}

