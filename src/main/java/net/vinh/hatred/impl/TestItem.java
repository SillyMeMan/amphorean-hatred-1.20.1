package net.vinh.hatred.impl;

import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.api.builders.DamageContextBuilder;
import net.vinh.hatred.util.AmphoreanHatredUtil;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            DamageContextBuilder builder = new DamageContextBuilder();

            builder.type(AmphoreanHatredUtil.getEntryFromKey(world, DamageTypes.EXPLOSION));
            builder.deathMessage(Text.of("died to something"));
            builder.addKilledDisplayNameToMsg();

            user.damage(1f, builder.build());
        }

        return TypedActionResult.success(user.getMainHandStack(), true);
    }
}
