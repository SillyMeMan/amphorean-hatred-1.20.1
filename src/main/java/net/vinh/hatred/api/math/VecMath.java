package net.vinh.hatred.api.math;

import net.minecraft.util.math.Vec3d;

public final class VecMath {
    private VecMath() {}

    public static Vec3d horizontal(Vec3d v) {
        return new Vec3d(v.x, 0, v.z);
    }

    public static Vec3d withY(Vec3d v, double y) {
        return new Vec3d(v.x, y, v.z);
    }

    public static Vec3d direction(Vec3d from, Vec3d to) {
        return to.subtract(from).normalize();
    }

    public static double horizontalDistance(Vec3d a, Vec3d b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static Vec3d rotateY(Vec3d vec, double degrees) {
        double rad = Math.toRadians(degrees);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vec3d(
                vec.x * cos - vec.z * sin,
                vec.y,
                vec.x * sin + vec.z * cos
        );
    }
}

