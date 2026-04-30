package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.util.Utils;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            DamageContextBuilder builder = new DamageContextBuilder();

            builder.type(world.getDamageSources().lightningBolt().getTypeRegistryEntry());

            user.damage(100f, builder.build());
        }

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
