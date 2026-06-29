package net.vinh.hatred.internal;

import net.fabricmc.loader.api.FabricLoader;
import net.vinh.hatred.api.registry.AutoRegisterable;

import java.lang.invoke.MethodHandles;
import java.util.List;

public final class AutoRegistry {
    public static void autoCommonBootstrap() throws IllegalAccessException {
        bootstrap(FabricLoader.getInstance().getEntrypoints("hatred:registry", Class.class), false);
    }

    public static void autoClientBootstrap() throws IllegalAccessException {
        bootstrap(FabricLoader.getInstance().getEntrypoints("hatred:registry", Class.class), true);
    }

    @SuppressWarnings("all")
    private static void bootstrap(List<Class> classes, boolean client) throws IllegalAccessException {
        for(Class<?> clazz : classes) {
            AutoRegisterable annotation = clazz.getAnnotation(AutoRegisterable.class);

            if(annotation != null) {
                if(annotation.client() && !client) continue;
                MethodHandles.lookup().ensureInitialized(clazz);
            } else {
                throw new IllegalArgumentException("Class " + clazz.getName() + " doesn't have the required AutoRegisterable annotation. Annotation must be added or the class must be removed from the 'hatred:registry' entrypoint");
            }
        }
    }
}
