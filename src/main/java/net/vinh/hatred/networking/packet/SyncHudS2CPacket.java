package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.client.screen.ClientHudState;
import net.vinh.hatred.api.client.screen.HudTextEntry;

public record SyncHudS2CPacket(boolean fullClear, Text text, int priority, int ticksRemaining, int color, boolean shadow) {
    public static final Identifier ID = new Identifier("hatred", "sync_hud");

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean hasEntry = buf.readBoolean();

        if (!hasEntry) {
            client.execute(ClientHudState::clear);
            return;
        }

        Text text = buf.readText();
        int priority = buf.readInt();
        int ticksRemaining = buf.readInt();
        int color = buf.readInt();
        boolean shadow = buf.readBoolean();

        HudTextEntry entry = new HudTextEntry(
                text,
                priority,
                ticksRemaining,
                color,
                shadow
        );

        client.execute(() -> ClientHudState.set(entry));
    }
}
