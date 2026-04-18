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
        Box box = entity.getBoundingBox();

        if (contains(box.getCenter())) return true;

        return contains(new Vec3d(box.minX, box.minY, box.minZ)) ||
                contains(new Vec3d(box.minX, box.minY, box.maxZ)) ||
                contains(new Vec3d(box.minX, box.maxY, box.minZ)) ||
                contains(new Vec3d(box.minX, box.maxY, box.maxZ)) ||
                contains(new Vec3d(box.maxX, box.minY, box.minZ)) ||
                contains(new Vec3d(box.maxX, box.minY, box.maxZ)) ||
                contains(new Vec3d(box.maxX, box.maxY, box.minZ)) ||
                contains(new Vec3d(box.maxX, box.maxY, box.maxZ));
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
