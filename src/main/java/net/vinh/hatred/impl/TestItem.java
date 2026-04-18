package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.api.damage.element.ElementalTypes;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            DamageContextBuilder builder = new DamageContextBuilder().type(ElementalTypes.getRandomTypeAndConvert(world)).bypassesArmor().bypassesEnchantments();

            user.damage(0.5, builder.build());
        }

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
