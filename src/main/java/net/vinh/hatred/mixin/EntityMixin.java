package net.vinh.hatred.mixin;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.vinh.hatred.api.collision.Barrier;
import net.vinh.hatred.api.collision.BarrierManager;
import net.vinh.hatred.api.damage.ContextAwareDamageSource;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.internal.HatredAttachments;
import net.vinh.hatred.internal.data.DataRegistryInternal;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.accessor.EntityMixinAccessor;
import net.vinh.hatred.internal.entity.EntityInjectionAccess;
import net.vinh.hatred.networking.packet.SyncAttachmentS2CPacket;
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
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin implements DataHolderInternal, EntityMixinAccessor, EntityInjectionAccess {
    private static World zaWaurdo;
    private static Entity idiot;

    @Shadow public abstract World getWorld();

    @Unique
    private DataContainer hatred$data;

    @Override
    public DataContainer hatred$getContainer() {
        if(hatred$data == null) {
            hatred$data = new DataContainer();
        }
        return hatred$data;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void hatred$initContainer(CallbackInfo ci) {
        if(hatred$data == null) {
            hatred$data = new DataContainer();
        }
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void hatred$invulnerability(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if(damageSource instanceof ContextAwareDamageSource ctx && ctx.context().bypassesInvulnerability()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeAttachments(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        DataContainer container = hatred$getContainer();
        if (container == null) return;

        NbtCompound libTag = new NbtCompound();

        for (var entry : container.entries().entrySet()) {
            DataAttachmentType type = entry.getKey();
            if (!type.persistent()) continue;

            NbtCompound sub = new NbtCompound();
            type.serializer().writeNbt(sub, entry.getValue());
            libTag.put(type.id().toString(), sub);
        }

        nbt.put("hatred:data", libTag);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readAttachments(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("hatred:data")) return;

        NbtCompound libTag = nbt.getCompound("hatred:data");

        DataContainer container = hatred$getContainer();

        if(container == null) return;

        for (DataAttachmentType<?> type : DataRegistryInternal.values()) {
            if (!type.persistent()) continue;

            if (libTag.contains(type.id().toString())) {
                NbtCompound sub = libTag.getCompound(type.id().toString());
                Object value = type.serializer().readNbt(sub);
                container.setRaw(type, value);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hatred$syncAttachments(CallbackInfo ci) {
        if (getWorld().isClient()) return;

        if (hatred$data == null) return;

        Set<DataAttachmentType<?>> dirty = hatred$data.consumeDirty();
        if (dirty.isEmpty()) return;

        Entity self = (Entity)(Object)this;

        for (ServerPlayerEntity player : PlayerLookup.tracking(self)) {

            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeVarInt(self.getId());
            buf.writeVarInt(dirty.size());

            for (DataAttachmentType<?> type : dirty) {
                buf.writeIdentifier(type.id());

                NbtCompound tag = new NbtCompound();
                writeSyncAttachment(type, hatred$data, tag);

                buf.writeNbt(tag);
            }

            ServerPlayNetworking.send(player, SyncAttachmentS2CPacket.ID, buf);
        }
    }

    @Override
    public void hatred$syncFull(ServerPlayerEntity player) {
        if (hatred$data == null) return;

        Map<DataAttachmentType<?>, Object> syncables =
                hatred$data.getAllSyncable();

        if (syncables.isEmpty()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        Entity self = (Entity)(Object)this;

        buf.writeVarInt(self.getId());
        buf.writeVarInt(syncables.size());

        for (Map.Entry<DataAttachmentType<?>, Object> entry : syncables.entrySet()) {
            DataAttachmentType<?> type = entry.getKey();

            buf.writeIdentifier(type.id());

            NbtCompound tag = new NbtCompound();
            writeSyncAttachments(type, entry.getValue(), tag);

            buf.writeNbt(tag);
        }

        ServerPlayNetworking.send(player, SyncAttachmentS2CPacket.ID, buf);
    }

    @Inject(method = "addVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeMovement(double x, double y, double z, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && Data.API.get(living, HatredAttachments.MOVEMENT_FROZEN)) {
            ci.cancel();
        }
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeYaw(float yaw, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && Data.API.get(living, HatredAttachments.ROTATION_LOCKED)) {
            ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    private void hatred$freezePitch(float pitch, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && Data.API.get(living, HatredAttachments.ROTATION_LOCKED)) {
            ci.cancel();
        }
    }

    @Inject(method = "setHeadYaw", at = @At("HEAD"), cancellable = true)
    private void hatred$freezeHeadYaw(float yaw, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;

        if (self instanceof LivingEntity living && Data.API.get(living, HatredAttachments.ROTATION_LOCKED)) {
            ci.cancel();
        }
    }

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"))
    private static void heHasRamGetHim(Entity entity, Vec3d movement, Box entityBoundingBox, World world, List<VoxelShape> collisions, CallbackInfoReturnable<Vec3d> cir) {
        zaWaurdo = world;
        idiot = entity;
    }

    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/common/collect/ImmutableList$Builder;addAll(Ljava/lang/Iterable;)Lcom/google/common/collect/ImmutableList$Builder;", ordinal = 1))
    private static ImmutableList.Builder<VoxelShape> hatred$collisions(ImmutableList.Builder<VoxelShape> original) {
        List<Barrier> barriers = BarrierManager.getCurrentBarriers();

        for(Barrier barrier : barriers) {
            if(barrier.blocks(idiot)) {
                original.add(barrier.shape());
            }
        }

        return original;
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static <T> void writeSyncAttachments(DataAttachmentType<T> type, Object value, NbtCompound tag) {
        type.serializer().writeNbt(tag, (T) value);
    }

    @Unique
    private static <T> void writeSyncAttachment(DataAttachmentType<T> type, DataContainer container, NbtCompound tag) {
        T value = container.get(type);
        type.serializer().writeNbt(tag, value);
    }
}