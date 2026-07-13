package net.vinh.hatred.mixin;

import net.minecraft.item.ItemStack;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public final class ItemStackMixin implements DataHolderInternal {
    @Unique
    private DataContainer hatred$data;

    @Override
    public DataContainer hatred$getContainer() {
        if(hatred$data == null) {
            hatred$data = new DataContainer();
        }
        return hatred$data;
    }
}
