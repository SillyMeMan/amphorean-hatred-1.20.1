package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.client.camera.ScreenshakeUtil;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return TypedActionResult.pass(user.getMainHandStack());

        ScreenshakeUtil.shake((ServerPlayerEntity) user, 100f, 10f, 60);

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
