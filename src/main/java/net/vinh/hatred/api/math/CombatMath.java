package net.vinh.hatred.api.math;

import net.minecraft.util.math.Vec3d;

public final class CombatMath {
    public static float linearFalloff(double distance, double maxDistance) {
        return (float) HatredMath.clamp(
                1.0 - (distance / maxDistance),
                0.0,
                1.0
        );
    }

    public static boolean isInCone(Vec3d origin, Vec3d forward, Vec3d target, double angleDegrees) {
        Vec3d toTarget = target.subtract(origin).normalize();
        double dot = forward.normalize().dotProduct(toTarget);

        double threshold = Math.cos(Math.toRadians(angleDegrees / 2));
        return dot > threshold;
    }

    public static float distributeDamageEvenly(float totalDamage, int targetCount) {
        if (targetCount <= 0) return 0;
        return totalDamage / targetCount;
    }

    public static float distributedWithScaling(float baseDamage, int targetCount, float scalingMultiplier) {
        if (targetCount <= 0) return 0;

        return baseDamage * scalingMultiplier;
    }
}
