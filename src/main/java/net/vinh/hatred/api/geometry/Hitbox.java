package net.vinh.hatred.api.geometry;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class Hitbox {
    public final Vec3d center;
    public final Vec3d halfExtents;

    public final Vec3d right;
    public final Vec3d up;
    public final Vec3d forward;

    /**
     * @param right Must be orthogonal
     * @param up Must be orthogonal
     * @param forward Must be orthogonal
     */
    public Hitbox(Vec3d center, Vec3d size, Vec3d right, Vec3d up, Vec3d forward) {
        this.center = center;
        this.halfExtents = size.multiply(0.5);

        this.right = right.normalize();
        this.up = up.normalize();
        this.forward = forward.normalize();
    }

    public static Hitbox fromYawPitch(Vec3d center, Vec3d size, float yaw, float pitch) {
        Vec3d forward = Vec3d.fromPolar(pitch, yaw).normalize();

        Vec3d worldUp = new Vec3d(0, 1, 0);

        if (Math.abs(forward.y) > 0.999) {
            worldUp = new Vec3d(1, 0, 0);
        }

        Vec3d right = forward.crossProduct(worldUp).normalize();
        Vec3d up = right.crossProduct(forward).normalize();

        return new Hitbox(center, size, right, up, forward);
    }

    public boolean intersects(Entity entity) {
        return intersects(entity.getBoundingBox());
    }

    public boolean intersects(Box box) {
        Vec3d boxCenter = box.getCenter();

        double boxHalfX = box.getXLength() * 0.5;
        double boxHalfY = box.getYLength() * 0.5;
        double boxHalfZ = box.getZLength() * 0.5;

        Vec3d[] axes = {
                new Vec3d(1, 0, 0),
                new Vec3d(0, 1, 0),
                new Vec3d(0, 0, 1),

                right,
                up,
                forward,

                new Vec3d(0, 1, 0).crossProduct(right),
                new Vec3d(0, 1, 0).crossProduct(up),
                new Vec3d(0, 1, 0).crossProduct(forward),

                new Vec3d(0, 0, 1).crossProduct(right),
                new Vec3d(0, 0, 1).crossProduct(up),
                new Vec3d(0, 0, 1).crossProduct(forward),

                new Vec3d(1, 0, 0).crossProduct(right),
                new Vec3d(1, 0, 0).crossProduct(up),
                new Vec3d(1, 0, 0).crossProduct(forward)
        };

        Vec3d centerDelta = boxCenter.subtract(center);

        for (Vec3d axis : axes) {
            if (axis.lengthSquared() < 1.0E-10) {
                continue;
            }

            axis = axis.normalize();

            double distance = Math.abs(centerDelta.dotProduct(axis));

            double obbRadius =
                    Math.abs(axis.dotProduct(right)) * halfExtents.x +
                            Math.abs(axis.dotProduct(up)) * halfExtents.y +
                            Math.abs(axis.dotProduct(forward)) * halfExtents.z;

            double boxRadius =
                    Math.abs(axis.x) * boxHalfX +
                            Math.abs(axis.y) * boxHalfY +
                            Math.abs(axis.z) * boxHalfZ;

            if (distance > obbRadius + boxRadius) {
                return false;
            }
        }

        return true;
    }

    public boolean contains(Vec3d point) {
        Vec3d local = toLocal(point);

        return Math.abs(local.x) <= halfExtents.x &&
                Math.abs(local.y) <= halfExtents.y &&
                Math.abs(local.z) <= halfExtents.z;
    }

    private Vec3d toLocal(Vec3d point) {
        Vec3d rel = point.subtract(center);

        return new Vec3d(
                rel.dotProduct(right),
                rel.dotProduct(up),
                rel.dotProduct(forward)
        );
    }
}
