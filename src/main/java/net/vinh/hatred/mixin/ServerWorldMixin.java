package net.vinh.hatred.mixin;

import net.minecraft.server.world.ServerWorld;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements DataHolderInternal {

    private DataContainer hatred$data;

    @Override
    public DataContainer hatred$getContainer() {
        if (hatred$data == null) {
            hatred$data = new DataContainer();
        }
        return hatred$data;
    }

    @Inject(method = "saveLevel", at = @At("TAIL"))
    private void saveData(CallbackInfo ci) {
        //TODO: add world persistence
    }
}

