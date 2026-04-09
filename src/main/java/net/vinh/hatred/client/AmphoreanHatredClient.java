package net.vinh.hatred.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.vinh.hatred.api.client.screen.HudTextEntry;
import net.vinh.hatred.client.animation.AnimationManager;
import net.vinh.hatred.networking.packet.ScreenshakeS2CPacket;
import net.vinh.hatred.networking.packet.SyncAttachmentS2CPacket;
import net.vinh.hatred.networking.packet.SyncHudS2CPacket;

public class AmphoreanHatredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> AnimationManager.tick());

        HudRenderCallback.EVENT.register(new HudTextEntry.HudTextRenderer());

        ClientPlayNetworking.registerGlobalReceiver(ScreenshakeS2CPacket.ID, ScreenshakeS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncAttachmentS2CPacket.ID, SyncAttachmentS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHudS2CPacket.ID, SyncHudS2CPacket::handle);
    }
}
