package net.vinh.hatred.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.vinh.hatred.networking.packet.ScreenshakeS2CPacket;
import net.vinh.hatred.networking.packet.SyncAttachmentS2CPacket;

public class AmphoreanHatredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenshakeS2CPacket.ID, ScreenshakeS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncAttachmentS2CPacket.ID, SyncAttachmentS2CPacket::handle);
    }
}
