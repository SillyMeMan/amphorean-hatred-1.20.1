package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.internal.data.DataContainer;
import net.vinh.hatred.internal.data.DataHolderInternal;
import net.vinh.hatred.internal.data.DataRegistryInternal;

public record SyncAttachmentS2CPacket(int entityId, int dirtySize, Identifier id, NbtCompound tag) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = new Identifier("hatred", "sync_attachment");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeVarInt(dirtySize);
        buf.writeIdentifier(id);
        buf.writeNbt(tag);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readVarInt();
        int count = buf.readVarInt();

        Identifier id = buf.readIdentifier();
        NbtCompound tag = buf.readNbt();

        client.execute(() -> {
            Entity entity = client.world.getEntityById(entityId);

            if (!(entity instanceof DataHolderInternal holder)) return;

            DataContainer container = holder.hatred$getContainer();

            for (int i = 0; i < count; i++) {
                DataAttachmentType<?> type = DataRegistryInternal.get(id);

                if (type == null) continue;

                applySync(type, container, tag);
            }
        });
    }

    private static <T> void applySync(DataAttachmentType<T> type, DataContainer container, NbtCompound tag) {
        T value = type.serializer().readNbt(tag);
        container.setRaw(type, value);
    }
}
