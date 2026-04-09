package net.vinh.hatred.api.math;

import net.minecraft.util.hit.HitResult;

@FunctionalInterface
public interface RaycastAction {
    boolean run(HitResult result);
}
