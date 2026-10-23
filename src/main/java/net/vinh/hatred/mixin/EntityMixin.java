package net.vinh.hatred.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.vinh.hatred.cca.ModComponents;
import net.vinh.hatred.api.collision.Barrier;
import net.vinh.hatred.api.collision.BarrierManager;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.internal.entity.EntityInjectionAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInjectionAccess {
    @Unique
    private static Entity idiot;

    @Shadow public abstract World getWorld();

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void hatred$invulnerability(
            DamageSource damageSource,
            CallbackInfoReturnable<Boolean> cir
    ) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof LivingEntity living)) {
            return;
        }

        boolean pureInvulnerable = ModComponents.LIVING_ENTITY.get(living).isPureInvulnerable;

        if (pureInvulnerable) {
            if (damageSource instanceof ContextAwareDamageSource ctx) {
                if (!ctx.context().bypassesPureInvulnerability()) {
                    cir.setReturnValue(true);
                    return;
                }
            } else {
                cir.setReturnValue(true);
                return;
            }
        }

        if (!(damageSource instanceof ContextAwareDamageSource ctx)) {
            if (ModComponents.LIVING_ENTITY.get(living).isInvulnerable
                    && !damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                cir.setReturnValue(true);
            }

            return;
        }

        var context = ctx.context();

        if (context.bypassesInvulnerability()) {
            cir.setReturnValue(false);
            return;
        }

        if (ModComponents.LIVING_ENTITY.get(living).isInvulnerable) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "addVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    private void hatred$blockVelocity(double x, double y, double z, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && ModComponents.LIVING_ENTITY.get(living).movementFrozen) {
            ci.cancel();
        }
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeYaw(float yaw, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && ModComponents.LIVING_ENTITY.get(living).rotationLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    private void hatred$freezePitch(float pitch, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && ModComponents.LIVING_ENTITY.get(living).rotationLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "setHeadYaw", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeHeadYaw(float yaw, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && ModComponents.LIVING_ENTITY.get(living).rotationLocked) {
            ci.cancel();
        }
    }

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"))
    private static void heHasRamGetHim(Entity entity, Vec3d movement, Box entityBoundingBox, World world, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3d> cir) {
        idiot = entity;
    }

    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/ImmutableList$Builder;addAll(Ljava/lang/Iterable;)Lcom/google/common/collect/ImmutableList$Builder;", ordinal = 1))
    private static ImmutableList.Builder<VoxelShape> hatred$collisions(ImmutableList.Builder<VoxelShape> original) {
        Map<RegistryKey<World>, Barrier> barriers = BarrierManager.getCurrentBarriers();

        barriers.forEach((worldRegistryKey, barrier) -> {
            if(idiot.getWorld().getRegistryKey() == worldRegistryKey && barrier.blocks(idiot)) {
                original.add(barrier.shape());
            }
        });

        return original;
    }
}