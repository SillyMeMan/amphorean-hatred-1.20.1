package net.vinh.hatred.api.collision;

import net.minecraft.entity.Entity;
import net.minecraft.util.shape.VoxelShape;

import java.util.function.Predicate;

public record Barrier(VoxelShape shape, Predicate<Entity> conditions) {
    public boolean blocks(Entity entity) {
        return this.conditions.test(entity);
    }
}
