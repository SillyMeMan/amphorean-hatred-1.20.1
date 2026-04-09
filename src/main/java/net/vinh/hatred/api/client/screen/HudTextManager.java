package net.vinh.hatred.api.client.screen;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vinh.hatred.networking.packet.SyncHudS2CPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HudTextManager {
    private static final Map<UUID, HudTextEntry> ACTIVE = new HashMap<>();

    public static void show(ServerPlayerEntity player, HudTextEntry entry) {
        UUID id = player.getUuid();
        HudTextEntry current = ACTIVE.get(id);

        if (current == null || current.isExpired() || entry.priority >= current.priority) {
            ACTIVE.put(id, entry);
            sync(player, entry);
        }
    }

    public static void cancel(ServerPlayerEntity player) {
        ACTIVE.remove(player.getUuid());
        sync(player, null);
    }

    public static void tick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID id = player.getUuid();
            HudTextEntry entry = ACTIVE.get(id);

            if (entry == null) continue;

            entry.tick();

            if (entry.isExpired()) {
                ACTIVE.remove(id);
                sync(player, null);
            }
        }
    }

    private static void sync(ServerPlayerEntity player, HudTextEntry entry) {
        PacketByteBuf buf = PacketByteBufs.create();

        if (entry == null) {
            buf.writeBoolean(false); // means CLEAR
        } else {
            buf.writeBoolean(true);  // means SET

            buf.writeText(entry.text);
            buf.writeInt(entry.priority);
            buf.writeInt(entry.ticksRemaining);
            buf.writeInt(entry.color);
            buf.writeBoolean(entry.shadow);
        }

        ServerPlayNetworking.send(player, SyncHudS2CPacket.ID, buf);
    }
}

