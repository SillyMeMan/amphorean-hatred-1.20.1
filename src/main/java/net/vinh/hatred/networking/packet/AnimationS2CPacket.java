package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.client.animation.AnimationController;
import net.vinh.hatred.client.animation.AnimationDefinition;
import net.vinh.hatred.client.animation.AnimationManager;

public record AnimationS2CPacket(int entityId, Identifier definitionId, Action action) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("animation_packet");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeIdentifier(this.definitionId);
        buf.writeEnumConstant(this.action);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readVarInt();
        Identifier animationId = buf.readIdentifier();
        Action action = buf.readEnumConstant(Action.class);

        client.execute(() -> {
            if (client.world == null) return;

            Entity entity = client.world.getEntityById(entityId);

            if (!(entity instanceof LivingEntity living)) return;

            AnimationController controller = AnimationManager.get(living);

            switch (action) {
                case PLAY -> {
                    AnimationDefinition definition = HatredRegistries.ANIMATION.get(animationId);
                    if (definition == null) return;
                    AmphoreanHatred.LOGGER.info("Played");
                    controller.play(definition);
                }
                case STOP -> controller.stop(animationId);
                case STOP_ALL -> controller.stopAll();
            }
        });
    }

    public enum Action {
        PLAY,
        STOP,
        STOP_ALL
    }
}
