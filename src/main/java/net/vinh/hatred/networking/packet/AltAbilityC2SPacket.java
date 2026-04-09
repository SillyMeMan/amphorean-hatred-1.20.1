package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vinh.hatred.util.AmphoreanHatredUtil;
import org.jetbrains.annotations.NotNull;

@Deprecated
public record AltAbilityC2SPacket(int alt_ability_number) implements Packet<ServerPlayPacketListener> {
    public static final Identifier ID = new Identifier("hatred", "alt_ability");

    @Override
    public void write(@NotNull PacketByteBuf buf) {
        buf.writeInt(alt_ability_number);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {

    }

    public static void handle(MinecraftServer server, @NotNull ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        int abilityNumber = buf.readInt();
        AmphoreanHatredUtil.useAbility(player, abilityNumber);
    }
}
