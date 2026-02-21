package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.client.camera.ScreenshakeController;
import org.jetbrains.annotations.Nullable;

public record ScreenshakeS2CPacket(float intensity, int duration, @Nullable Vec3d origin, float radius) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = new Identifier("hatred", "screenshake_packet");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(intensity);
        buf.writeVarInt(duration);

        buf.writeBoolean(origin != null);
        if (origin != null) {
            buf.writeDouble(origin.x);
            buf.writeDouble(origin.y);
            buf.writeDouble(origin.z);
        }

        buf.writeFloat(radius);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        float intensity = buf.readFloat();
        int duration = buf.readVarInt();

        boolean hasOrigin = buf.readBoolean();
        Vec3d origin = hasOrigin ? new Vec3d(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
        ) : null;

        float radius = buf.readFloat();

        client.execute(() -> {
            if (origin != null && client.player != null) {
                double dist = client.player.getPos().distanceTo(origin);
                if (dist > radius) return;

                float falloff = 1f - (float)(dist / radius);
                ScreenshakeController.shake(intensity * falloff, duration);
            } else {
                ScreenshakeController.shake(intensity, duration);
            }
        });
    }
}
