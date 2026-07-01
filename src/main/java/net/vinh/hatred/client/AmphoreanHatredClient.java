package net.vinh.hatred.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.vinh.hatred.api.client.screen.HudTextEntry;
import net.vinh.hatred.client.animation.AnimationManager;
import net.vinh.hatred.internal.AutoRegistry;
import net.vinh.hatred.internal.util.ClientCrashHandler;
import net.vinh.hatred.networking.packet.*;

public class AmphoreanHatredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoRegistry.autoClientBootstrap();

        ClientPlayNetworking.registerGlobalReceiver(ScreenshakeS2CPacket.ID, ScreenshakeS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncAttachmentS2CPacket.ID, SyncAttachmentS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHudS2CPacket.ID, SyncHudS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(CrashS2CPacket.ID, CrashS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(ShutdownS2CPacket.ID, ShutdownS2CPacket::handle);
    }
}
