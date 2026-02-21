package net.vinh.hatred.mixin;

import net.minecraft.item.ItemStack;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin implements DataHolderInternal {
    private DataContainer hatred$data;

    @Override
    public DataContainer hatred$getContainer() {
        if(hatred$data == null) {
            hatred$data = new DataContainer();
        }
        return hatred$data;
    }
}
