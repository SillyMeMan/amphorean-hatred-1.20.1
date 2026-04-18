package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;

// For you mad men
public class CrashS2CPacket implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("crash");

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public void apply(ClientPlayPacketListener listener) {}

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(CrashS2CPacket::handle$lambda);
    }

    private static void handle$lambda() {
        GlfwUtil.makeJvmCrash();
    }
}
