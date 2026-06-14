package net.vinh.hatred.mixin;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.vinh.hatred.api.event.WorldBossEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow @Final private ServerWorld world;
    @Shadow @Final private BlockPos origin;
    @Shadow private boolean previouslyKilled;

    @Inject(method = "createDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void hatred$onDragonSpawn(CallbackInfoReturnable<EnderDragonEntity> cir) {
        WorldBossEvents.DRAGON_SPAWN.invoker().onDragonSpawn(world, origin, !previouslyKilled);
    }

    @Inject(method = "dragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateNewEndGateway()V"))
    private void hatred$onDragonKilled(EnderDragonEntity dragon, CallbackInfo ci) {
        WorldBossEvents.DRAGON_KILLED.invoker().onDragonKilled(world, dragon.getLastAttacker(), !previouslyKilled);
    }

    @Inject(method = "respawnDragon(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateEndPortal(Z)V"))
    private void hatred$onStartingRespawnSequence(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        WorldBossEvents.DRAGON_RESPAWN.invoker().onStartingRespawnSequence(world);
    }

    @Inject(method = "crystalDestroyed", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;debug(Ljava/lang/String;)V"))
    private void hatred$onRespawnAborted(EndCrystalEntity enderCrystal, DamageSource source, CallbackInfo ci) {
        WorldBossEvents.DRAGON_RESPAWN_ABORTED.invoker().onRespawnAborted(world, enderCrystal);
    }
}
