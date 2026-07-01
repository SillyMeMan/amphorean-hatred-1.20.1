package net.vinh.hatred.internal;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.registry.AutoInitializer;

import java.lang.invoke.MethodHandles;

public final class AutoRegistry {
    public static void autoServerBootstrap() {
        bootstrap(Environment.SERVER);
    }

    public static void autoClientBootstrap() {
        bootstrap(Environment.CLIENT);
    }

    public static void autoCommonBootstrap() {
        bootstrap(Environment.COMMON);
    }

    private static void bootstrap(Environment environment) {
        for(ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            CustomValue value = mod.getMetadata().getCustomValue("hatred:registry");
            if (value == null) continue;

            try {
                String key = switch (environment) {
                    case COMMON -> "common";
                    case CLIENT -> "client";
                    case SERVER -> "server";
                };

                AmphoreanHatred.LOGGER.debug(
                        "Loading {} registry classes for {}",
                        key,
                        mod.getMetadata().getId()
                );

                CustomValue.CvObject registry = value.getAsObject();
                load(registry, key);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed loading registry for mod " + mod.getMetadata().getId(), e);
            }
        }
    }

    private static void load(CustomValue.CvObject registry, String key) throws ReflectiveOperationException {
        CustomValue value = registry.get(key);

        if (value == null)
            return;

        if (value.getType() != CustomValue.CvType.ARRAY) {
            throw new IllegalStateException(
                    "'" + key + "' must be an array"
            );
        }

        for (CustomValue entry : value.getAsArray()) {
            String className = entry.getAsString();

            Class<?> clazz = Class.forName(
                    className,
                    false,
                    AutoInitializer.class.getClassLoader());

            AutoInitializer annotation =
                    clazz.getAnnotation(AutoInitializer.class);

            if (annotation == null) {
                throw new IllegalStateException(
                        clazz.getName() + " is missing @AutoRegisterable");
            }

            MethodHandles.lookup().ensureInitialized(clazz);
        }
    }

    private enum Environment {
        COMMON,
        CLIENT,
        SERVER
    }
}
