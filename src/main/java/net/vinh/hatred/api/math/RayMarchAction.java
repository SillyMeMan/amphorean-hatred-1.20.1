package net.vinh.hatred.api.math;

import net.minecraft.util.hit.HitResult;

@FunctionalInterface
public interface RayMarchAction {
    boolean run(int distanceFromStart, HitResult result);
}
