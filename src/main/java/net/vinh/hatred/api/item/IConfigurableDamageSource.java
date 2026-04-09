package net.vinh.hatred.api.item;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IConfigurableDamageSource {
    DamageSource createSource(PlayerEntity player);

    boolean trueDamage();

    boolean bypassArmor();
    boolean bypassResistance();
    boolean bypassInvulnerability();
    boolean bypassTotems();
    @Nullable List<StatusEffectInstance> hitEffects();
    @Nullable Vec3d knockback();
}
