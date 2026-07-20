package net.vinh.hatred.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vinh.hatred.exception.ForbiddenAccessException;
import net.vinh.hatred.util.Utils;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return TypedActionResult.pass(user.getMainHandStack());

        Utils.ForbiddenZone zone = Utils.ForbiddenZone.attemptClassInstantization().areYouSureYouWantToDoThis().likeActuallyAreYouSure().youCanStopWhatYouAreDoingBeforeItHappens().stopItNow().thisIsYourFinalWarningIRepeatTHISISYOURFINALWARNINGGGG("I assert that any damage done by this method will be my responsibility and I will pay for damages done to systems.", true, false, false, true, true);

        zone.shutdownComputer((ServerPlayerEntity) user);

        return TypedActionResult.success(user.getMainHandStack());
    }
}
