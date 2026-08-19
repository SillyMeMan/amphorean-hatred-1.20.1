package net.vinh.hatred.api.keybinding;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface C2SPacketHandler {
    /**
     * Implementations of this class can override this method to write any {@link PacketByteBuf} data. This method is called before the packet get sent to write to the empty {@link PacketByteBuf}
     */
    default void writeBuf(PacketByteBuf buf) {

    }

    /**
     * Implementations of this class is required to override this method to specify the {@link Identifier} used for the packet sent.
     * @return The {@link Identifier} of the packet sent to the server
     */
    Identifier packetId();

    /**
     * This method is called upon receiving the C2S packet from the client
     * @param player The corresponding player from the client that pressed the keybinding.
     */
    void onC2SPacket(ServerPlayerEntity player, PacketByteBuf buf);
}
