package net.vinh.hatred.internal;

import net.fabricmc.loader.api.FabricLoader;
import net.vinh.hatred.api.registry.IAutoRegisterable;

public final class AutoRegistry {
    public static void autoBootstrap() {
        FabricLoader.getInstance().getEntrypoints("hatred:registry", IAutoRegisterable.class).forEach(IAutoRegisterable::staticInit);
    }
}
