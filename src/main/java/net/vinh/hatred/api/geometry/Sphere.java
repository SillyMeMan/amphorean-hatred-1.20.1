package net.vinh.hatred.api.geometry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Sphere {
	public final double x;
	public final double y;
	public final double z;
	public final double radius;

	public Sphere(double x, double y, double z, double radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
	}

	public Sphere(Vec3d center, double radius) {
		this(center.x, center.y, center.z, radius);
	}

	public Sphere(BlockPos pos, double radius) {
		this(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), radius);
	}

	// ----------------------------------------
	// Basic Utilities
	// ----------------------------------------
	public Vec3d center() {
		return new Vec3d(x, y, z);
	}

	public Box toBox() {
		return new Box(
			x - radius, y - radius, z - radius,
			x + radius, y + radius, z + radius
		);
	}

	public Sphere offset(double ox, double oy, double oz) {
		return new Sphere(x + ox, y + oy, z + oz, radius);
	}

	public Sphere offset(Vec3d v) {
		return new Sphere(x + v.x, y + v.y, z + v.z, radius);
	}

	public Sphere expand(double amount) {
		return new Sphere(x, y, z, radius + amount);
	}

	// ----------------------------------------
	// Contains / Intersects
	// ----------------------------------------
	public boolean contains(double px, double py, double pz) {
		double dx = px - x;
		double dy = py - y;
		double dz = pz - z;
		return dx*dx + dy*dy + dz*dz <= radius * radius;
	}

	public boolean contains(Vec3d p) {
		return contains(p.x, p.y, p.z);
	}

	public boolean intersects(Sphere other) {
		double dx = other.x - x;
		double dy = other.y - y;
		double dz = other.z - z;
		double rr = (other.radius + radius);
		return dx*dx + dy*dy + dz*dz <= rr * rr;
	}

	public boolean intersects(Box box) {
		double cx = MathHelper.clamp(x, box.minX, box.maxX);
		double cy = MathHelper.clamp(y, box.minY, box.maxY);
		double cz = MathHelper.clamp(z, box.minZ, box.maxZ);

		double dx = cx - x;
		double dy = cy - y;
		double dz = cz - z;

		return dx*dx + dy*dy + dz*dz <= radius * radius;
	}

	// ----------------------------------------
	// Ray Intersection
	// ----------------------------------------
	public HitResult raycast(Vec3d origin, Vec3d direction) {
		Vec3d oc = origin.subtract(center());

		double a = direction.lengthSquared();
		double b = 2.0 * oc.dotProduct(direction);
		double c = oc.lengthSquared() - radius * radius;

		double discriminant = b*b - 4*a*c;
		if (discriminant < 0) return null;

		double sqrt = Math.sqrt(discriminant);
		double t1 = (-b - sqrt) / (2*a);
		double t2 = (-b + sqrt) / (2*a);

		if (t1 >= 0) {
			return new BlockHitResult(origin.add(direction.multiply(t1)), Direction.getFacing(direction.x, direction.y, direction.z), BlockPos.ofFloored(center().getX(), center().getY(), center().getZ()), false);
		}
		if (t2 >= 0) {
			return new BlockHitResult(origin.add(direction.multiply(t2)), Direction.getFacing(direction.x, direction.y, direction.z), BlockPos.ofFloored(center().getX(), center().getY(), center().getZ()), false);
		}

		return null;
	}

	// ----------------------------------------
	// Entities / Blocks in sphere
	// ----------------------------------------
	public <T extends Entity> List<T> getEntities(Class<T> entityClass, World world, Predicate<Entity> filter) {
		List<T> list = world.getEntitiesByType(TypeFilter.instanceOf(entityClass), toBox(), filter);
		list.removeIf(e -> !contains(e.getPos()));
		return list;
	}

	public <T extends Entity> List<T> getEntities(Class<T> entityClass, World world) {
		return getEntities(entityClass, world, e -> true);
	}

	public boolean intersectsBlocks(World world) {
		for (BlockPos pos : BlockPos.iterate(MathHelper.floor(x - radius), MathHelper.floor(y - radius), MathHelper.floor(z - radius), MathHelper.floor(x + radius), MathHelper.floor(y + radius), MathHelper.floor(z + radius))) {
			if (contains(Vec3d.ofCenter(pos))) return true;
		}
		return false;
	}

	public List<BlockPos> getBlocks(World world, Predicate<BlockState> filter, FilterMode mode) {
		List<BlockPos> result = new ArrayList<>();

		int r = MathHelper.floor(radius);
		int r2 = r * r;

		int minX = MathHelper.floor(x) - r;
		int minY = MathHelper.floor(y) - r;
		int minZ = MathHelper.floor(z) - r;

		int maxX = MathHelper.floor(x) + r;
		int maxY = MathHelper.floor(y) + r;
		int maxZ = MathHelper.floor(z) + r;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int bx = minX; bx <= maxX; bx++) {
			double dx = (bx + 0.5) - x;
			double dx2 = dx * dx;

			if (dx2 > r2) continue;

			for (int by = minY; by <= maxY; by++) {
				double dy = (by + 0.5) - y;
				double dy2 = dy * dy;

				if (dx2 + dy2 > r2) continue;

				for (int bz = minZ; bz <= maxZ; bz++) {
					double dz = (bz + 0.5) - z;
					double dist2 = dx2 + dy2 + dz * dz;

					if (dist2 > r2) continue;

					pos.set(bx, by, bz);
					BlockState state = world.getBlockState(pos);

					boolean passes = filter.test(state);
					if (mode == FilterMode.IGNORE) passes = !passes;

					if (passes) {
						result.add(pos.toImmutable());
					}
				}
			}
		}

		return result;
	}

	// ----------------------------------------
	// Closest point on sphere surface
	// ----------------------------------------
	public Vec3d closestSurfacePointTo(Vec3d point) {
		Vec3d dir = point.subtract(center()).normalize();
		return center().add(dir.multiply(radius));
	}

	public enum FilterMode {
		IGNORE, KEEP
	}
}
