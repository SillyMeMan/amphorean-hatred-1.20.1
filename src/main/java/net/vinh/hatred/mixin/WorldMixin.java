package net.vinh.hatred.mixin;

import net.minecraft.world.World;
import net.vinh.hatred.internal.world.WorldInjectionAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(World.class)
public abstract class WorldMixin implements WorldInjectionAccess {
}
