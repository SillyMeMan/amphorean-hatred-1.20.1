package net.vinh.hatred.api.client.camera;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.vinh.hatred.networking.packet.ScreenshakeS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScreenshakeUtil {
    public static void shake(ServerPlayerEntity player, float radius, float intensity, int duration) {
        shake(List.of(player), null, radius, intensity, duration);
    }

    public static void shake(ServerPlayerEntity player, @Nullable Vec3d origin, float radius, float intensity, int duration) {
        shake(List.of(player), origin, radius, intensity, duration);
    }

    public static void shake(List<ServerPlayerEntity> players, @Nullable Vec3d origin, float radius, float intensity, int duration) {
        ScreenshakeS2CPacket packet = new ScreenshakeS2CPacket(intensity, duration, origin, radius);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        packet.write(buf);

        players.forEach(player -> ServerPlayNetworking.send(player, ScreenshakeS2CPacket.ID, buf));
    }
}
