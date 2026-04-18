package net.vinh.hatred.internal.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.vinh.hatred.api.geometry.Hitbox;
import net.vinh.hatred.api.math.RayMarchAction;
import net.vinh.hatred.api.math.RaycastAction;
import net.vinh.hatred.api.math.RaycastMath;

import java.util.List;
import java.util.function.Predicate;

public interface WorldInjectionAccess {
    default HitResult rayMarchFromEyes(RayMarchAction action, LivingEntity entity, int maxRange, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        ServerWorld world = (ServerWorld) this;

        return RaycastMath.rayMarchFromEyes(action, world, entity, maxRange, shapeType, fluidHandling);
    }

    default HitResult raycastFromEyes(RaycastAction action, LivingEntity entity, int maxRange, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        ServerWorld world = (ServerWorld) this;

        return RaycastMath.raycastFromEyes(action, world, entity, maxRange, shapeType, fluidHandling);
    }

    default List<Entity> getOtherEntities(Entity except, Hitbox hitbox, Predicate<? super Entity> predicate) {
        World world = (World) this;

        Vec3d r = hitbox.right.multiply(hitbox.halfExtents.x);
        Vec3d u = hitbox.up.multiply(hitbox.halfExtents.y);
        Vec3d f = hitbox.forward.multiply(hitbox.halfExtents.z);

        double x = Math.abs(r.x) + Math.abs(u.x) + Math.abs(f.x);
        double y = Math.abs(r.y) + Math.abs(u.y) + Math.abs(f.y);
        double z = Math.abs(r.z) + Math.abs(u.z) + Math.abs(f.z);

        Vec3d extents = new Vec3d(x, y, z);

        Box aabb = new Box(
                hitbox.center.subtract(extents),
                hitbox.center.add(extents)
        );

        List<Entity> candidates = world.getOtherEntities(except, aabb, predicate);

        return candidates.stream()
                .filter(hitbox::intersects)
                .toList();
    }

    default List<Entity> getOtherEntities(Entity except, Hitbox hitbox) {
        return this.getOtherEntities(except, hitbox, EntityPredicates.EXCEPT_SPECTATOR);
    }
}
