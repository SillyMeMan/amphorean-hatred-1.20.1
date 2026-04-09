package net.vinh.hatred.api.math;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public final class RaycastMath {
    @Nullable
    public static HitResult rayMarchFromEyes(RayMarchAction action, ServerWorld world, LivingEntity entity, int maxRange, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        int maxY = 0;
        Vec3d start = entity.getEyePos();

        while (maxY++ < maxRange) {
            Vec3d end = start.add(entity.getRotationVec(1.0F));
            HitResult hitResult = world.raycast(new RaycastContext(
                    start,
                    end,
                    shapeType,
                    fluidHandling,
                    entity
            ));

            if(action.run(maxY, hitResult)) return hitResult;

            start = end;
        }

        return BlockHitResult.createMissed(start, Direction.UP, BlockPos.ofFloored(start));
    }

    public static HitResult raycastFromEyes(RaycastAction action, ServerWorld world, LivingEntity entity, int maxRange, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        Vec3d start = entity.getEyePos();
        Vec3d rotVec = entity.getRotationVec(1.0F);
        Vec3d end = start.add(rotVec.x * maxRange, rotVec.y * maxRange, rotVec.z * maxRange);
        HitResult hitResult = world.raycast(new RaycastContext(
                start,
                end,
                shapeType,
                fluidHandling,
                entity
        ));

        action.run(hitResult);

        return hitResult;
    }
}
