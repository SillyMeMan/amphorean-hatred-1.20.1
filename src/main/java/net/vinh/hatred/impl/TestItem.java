package net.vinh.hatred.impl;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.geometry.Cylinder;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return TypedActionResult.pass(user.getMainHandStack());

        Cylinder cylinder = new Cylinder(user.getBlockPos(), 5, 50, 10);
        cylinder.getBlocks(world, AbstractBlock.AbstractBlockState::isAir, Cylinder.FilterMode.IGNORE).forEach(pos -> world.setBlockState(pos, Blocks.AIR.getDefaultState()));

        return TypedActionResult.success(user.getMainHandStack());
    }
}
