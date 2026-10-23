package net.vinh.hatred.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.util.Args;
import net.vinh.hatred.internal.ability.AbilityTimeline;

public class ExampleAbility extends Ability {
    public ExampleAbility() {
        super(500, 30, 300);
    }

    @Override
    public void cast(AbilityTimeline timeline, LivingEntity caster, Args args) {
        timeline.atTick(0, () -> {
            if(caster instanceof ServerPlayerEntity) {
                caster.sendMessage(Text.of("0"));
            }
        });

        timeline.atTick(100, () -> {
            if(caster instanceof ServerPlayerEntity) {
                caster.sendMessage(Text.of("100"));
            }
        });

        timeline.atTick(200, () -> {
            if(caster instanceof ServerPlayerEntity) {
                caster.sendMessage(Text.of("200"));
            }
        });

        timeline.atTick(200, () -> {
            if(!caster.getWorld().isClient()) {
                caster.getWorld().createExplosion(caster, caster.getX(), caster.getY(), caster.getZ(), 10f, true, World.ExplosionSourceType.TNT);
            }
        });

        timeline.atTick(300, () -> {
            if(caster instanceof ServerPlayerEntity) {
                caster.sendMessage(Text.of("300"));
            }
        });
    }

    @Override
    public void onFinished(LivingEntity caster) {
        if(!caster.getWorld().isClient()) {
            caster.getWorld().createExplosion(caster, caster.getX(), caster.getY(), caster.getZ(), 20f, true, World.ExplosionSourceType.TNT);
        }
    }

    @Override
    public void onCancelled(LivingEntity caster) {
        if(!caster.getWorld().isClient()) {
            caster.getWorld().createExplosion(caster, caster.getX(), caster.getY(), caster.getZ(), 50f, true, World.ExplosionSourceType.TNT);
        }
    }
}
