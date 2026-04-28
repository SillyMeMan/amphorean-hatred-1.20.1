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
import net.vinh.hatred.internal.util.ClientCrashHandler;
import net.vinh.hatred.networking.packet.CrashS2CPacket;
import net.vinh.hatred.networking.packet.ScreenshakeS2CPacket;
import net.vinh.hatred.networking.packet.SyncAttachmentS2CPacket;
import net.vinh.hatred.networking.packet.SyncHudS2CPacket;

public class AmphoreanHatredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> AnimationManager.tick());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(ClientCrashHandler.shouldCrash) {
                CrashReport crashReport = new CrashReport(ClientCrashHandler.reason, new Throwable(ClientCrashHandler.reason));
                CrashReportSection crashReportSection = crashReport.addElement("Crash details");
                WinNativeModuleUtil.addDetailTo(crashReportSection);
                throw new CrashException(crashReport);
            }
        });

        HudRenderCallback.EVENT.register(new HudTextEntry.HudTextRenderer());

        ClientPlayNetworking.registerGlobalReceiver(ScreenshakeS2CPacket.ID, ScreenshakeS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncAttachmentS2CPacket.ID, SyncAttachmentS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHudS2CPacket.ID, SyncHudS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(CrashS2CPacket.ID, CrashS2CPacket::handle);
    }
}
