package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.data.Data;
import net.vinh.hatred.attachment.HatredDataAttachmentTypes;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            Data.API.get(world, HatredDataAttachmentTypes.WORLD_SCHEDULER).schedule(100, () -> {
                world.createExplosion(null, user.getX(), user.getY(), user.getZ(), 10f, World.ExplosionSourceType.TNT);
            });
        }

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
