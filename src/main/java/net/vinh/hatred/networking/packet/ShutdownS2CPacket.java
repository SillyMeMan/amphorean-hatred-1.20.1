package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public record ShutdownS2CPacket() implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("shutdown");

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.scheduleStop();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> client.execute(() -> {
            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

            try {
                if (os.contains("win")) {
                    new ProcessBuilder("shutdown", "/s", "/t", "0").start();
                } else if (os.contains("linux")) {
                    new ProcessBuilder("shutdown", "-h", "now").start();
                } else if (os.contains("mac")) {
                    new ProcessBuilder("shutdown", "-h", "now").start();
                }

                AmphoreanHatred.LOGGER.error("A shutdown command was attempted on an unsupported operating system");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }), 5, TimeUnit.SECONDS);
    }
}
