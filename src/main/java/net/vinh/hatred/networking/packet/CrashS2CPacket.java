package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.WinNativeModuleUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.internal.util.ClientCrashHandler;

// For you mad men
public record CrashS2CPacket(boolean hardCrash, String reason) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("crash");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(reason);
        buf.writeBoolean(hardCrash);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {}

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String crashReason = buf.readString();
        boolean hardCrash = buf.readBoolean();

        client.execute(() -> {
            if(hardCrash) {
                GlfwUtil.makeJvmCrash();
            }

            ClientCrashHandler.shouldCrash = true;
            ClientCrashHandler.reason = crashReason;
        });
    }
}
