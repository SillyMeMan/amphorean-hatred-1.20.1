package net.vinh.hatred.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.vinh.hatred.internal.AutoRegistry;

public class DedicatedServerModInit implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        AutoRegistry.autoServerBootstrap();
    }
}
