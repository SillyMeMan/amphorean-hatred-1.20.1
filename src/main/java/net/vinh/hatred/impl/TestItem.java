package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.vinh.hatred.api.builders.DamageContextBuilder;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) {
            Vec3d pos = user.getPos();

            Random random = Random.createLocal();

            // 🔥 CORE FLASH
            for (int i = 0; i < 30; i++) {
                world.addParticle(
                        ParticleTypes.FLASH,
                        0, pos.y, 0,
                        0, 0, 0
                );
            }

            // 💥 MAIN BURST
            for (int i = 0; i < 120; i++) {
                double angle = random.nextDouble() * Math.PI * 2;
                double speed = 0.8 + random.nextDouble() * 3.0;

                double vx = Math.cos(angle) * speed;
                double vz = Math.sin(angle) * speed;
                double vy = (random.nextDouble() - 0.5) * 0.6;

                world.addParticle(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        0, pos.y, 0,
                        vx, vy, vz
                );
            }

            // 🌫️ SMOKE RING
            for (int i = 0; i < 80; i++) {
                double angle = (i / 80.0) * Math.PI * 2;

                double vx = Math.cos(angle) * 1.2;
                double vz = Math.sin(angle) * 1.2;

                world.addParticle(
                        ParticleTypes.CLOUD,
                        0, pos.y, 0,
                        vx, 0.1, vz
                );
            }

            for (int i = 0; i < 100; i++) {
                double angle = (i / 100.0) * Math.PI * 2;

                double vx = Math.cos(angle) * 2.5;
                double vz = Math.sin(angle) * 2.5;

                world.addParticle(
                        ParticleTypes.EXPLOSION,
                        0, pos.y, 0,
                        vx, 0, vz
                );
            }

            // ✨ SPARKS
            for (int i = 0; i < 60; i++) {
                double vx = (random.nextDouble() - 0.5) * 2.5;
                double vy = random.nextDouble() * 1.5;
                double vz = (random.nextDouble() - 0.5) * 2.5;

                world.addParticle(
                        ParticleTypes.END_ROD,
                        0, pos.y, 0,
                        vx, vy, vz
                );
            }
        }

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
