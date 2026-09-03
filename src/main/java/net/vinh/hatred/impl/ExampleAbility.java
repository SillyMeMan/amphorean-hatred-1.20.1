package net.vinh.hatred.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.ability.Ability;
import net.vinh.hatred.api.util.Args;

public class ExampleAbility extends Ability {
    public ExampleAbility() {
        super(200, 200, 100);
    }

    @Override
    public Args preCast(LivingEntity caster) {
        if(!caster.getWorld().isClient()) {
            AmphoreanHatred.LOGGER.info("Precast");

            caster.getWorld().createExplosion(null, caster.getX(), caster.getY(), caster.getZ(), 10f, World.ExplosionSourceType.TNT);
        }

        return Args.ofImmutable("shit");
    }

    @Override
    public void cast(LivingEntity caster, Args arguments) {
        if(!caster.getWorld().isClient()) {
            AmphoreanHatred.LOGGER.info("Cast");
        }
    }
}
