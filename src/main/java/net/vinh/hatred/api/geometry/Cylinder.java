package net.vinh.hatred.api.geometry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Cylinder {
    public final double x;
    public final double y;
    public final double z;
    public final double radius;

    public final double depth;
    public final double height;

    public Cylinder(double x, double y, double z, double radius, double depth, double height) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.depth = depth;
        this.height = height;
    }

    public Cylinder(BlockPos pos, double radius, double depth, double height) {
        this(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), radius, depth, height);
    }

    public Cylinder(Vec3d center, double radius, double depth, double height) {
        this(center.x, center.y, center.z, radius, depth, height);
    }

    public Vec3d center() {
        return new Vec3d(x, y, z);
    }

    public Box toBox() {
        return new Box(
                x - radius, y - depth, z - radius,
                x + radius, y + height, z + radius
        );
    }

    public Cylinder offset(double ox, double oy, double oz) {
        return new Cylinder(x + ox, y + oy, z + oz, radius, depth, height);
    }

    public Cylinder offset(Vec3d v) {
        return new Cylinder(x + v.x, y + v.y, z + v.z, radius, depth, height);
    }

    public Cylinder heighten(double amount) {
        return new Cylinder(x, y, z, radius, depth, height + amount);
    }

    public Cylinder deepen(double amount) {
        return new Cylinder(x, y, z, radius, depth + amount, height);
    }

    public Cylinder expand(double amount) {
        return new Cylinder(x, y, z, radius + amount, depth, height);
    }

    public boolean contains(double px, double py, double pz) {
        double dx = px - x;
        double dz = pz - z;

        double horizontalDistSq = dx * dx + dz * dz;

        return horizontalDistSq <= radius * radius
                && py >= y - depth
                && py <= y + height;
    }

    public boolean contains(Vec3d pos) {
        return contains(pos.x, pos.y, pos.z);
    }

    public <T extends Entity> List<T> getEntities(Class<T> entityClass, World world, Predicate<Entity> filter, FilterMode mode) {
        List<T> list = world.getEntitiesByType(TypeFilter.instanceOf(entityClass), toBox(), mode == FilterMode.KEEP ? filter : filter.negate());
        list.removeIf(e -> !contains(e.getPos()));
        return list;
    }

    public <T extends Entity> List<T> getEntities(Class<T> entityClass, World world) {
        return getEntities(entityClass, world, e -> true, FilterMode.KEEP);
    }

    public boolean intersectsBlocks() {
        for (BlockPos pos : BlockPos.iterate(MathHelper.floor(x - radius), MathHelper.floor(y - depth), MathHelper.floor(z - radius), MathHelper.floor(x + radius), MathHelper.floor(y + height), MathHelper.floor(z + radius))) {
            if (contains(Vec3d.ofCenter(pos))) return true;
        }
        return false;
    }

    public List<BlockPos> getBlocks(World world, Predicate<BlockState> filter, FilterMode mode) {
        List<BlockPos> result = new ArrayList<>();

        int r = MathHelper.floor(radius);
        int r2 = r * r;

        int minX = MathHelper.floor(x) - r;
        int maxX = MathHelper.floor(x) + r;

        int minY = MathHelper.floor(y - depth);
        int maxY = MathHelper.floor(y + height);

        int minZ = MathHelper.floor(z) - r;
        int maxZ = MathHelper.floor(z) + r;

        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int bx = minX; bx <= maxX; bx++) {
            double dx = (bx + 0.5) - x;
            double dx2 = dx * dx;

            if (dx2 > r2) continue;

            for (int bz = minZ; bz <= maxZ; bz++) {
                double dz = (bz + 0.5) - z;
                double dist2 = dx2 + dz * dz;

                if (dist2 > r2) continue;

                for (int by = minY; by <= maxY; by++) {
                    pos.set(bx, by, bz);

                    BlockState state = world.getBlockState(pos);

                    boolean passes = filter.test(state);
                    if (mode == FilterMode.IGNORE) {
                        passes = !passes;
                    }

                    if (passes) {
                        result.add(pos.toImmutable());
                    }
                }
            }
        }

        return result;
    }

    public Vec3d closestSurfacePointTo(Vec3d point) {
        Vec3d dir = point.subtract(center()).normalize();
        return center().add(dir.multiply(radius));
    }

    public enum FilterMode {
        IGNORE, KEEP
    }
}
