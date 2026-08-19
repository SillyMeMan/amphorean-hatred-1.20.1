package net.vinh.hatred.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.vinh.hatred.api.keybinding.C2SPacketHandler;

import java.util.ArrayList;
import java.util.List;

public final class C2SPacketHelper {
    private static final List<C2SPacketHandler> HANDLERS = new ArrayList<>();

    public static <T extends C2SPacketHandler> T register(T handler) {
        if(HANDLERS.stream().anyMatch(c2SPacketHandler -> c2SPacketHandler.packetId().equals(c2SPacketHandler.packetId()))) throw new IllegalArgumentException(
                "Duplicate C2S handler packet ID: " + handler.packetId()
        );

        HANDLERS.add(handler);
        return handler;
    }

    public static void initializeServer() {
        for (C2SPacketHandler handler : HANDLERS) {
            ServerPlayNetworking.registerGlobalReceiver(handler.packetId(), (server, player, networkHandler, buf, sender) -> server.execute(() -> handler.onC2SPacket(player, buf)));
        }
    }
}
