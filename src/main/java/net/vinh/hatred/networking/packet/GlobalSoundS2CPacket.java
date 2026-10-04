package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;

public record GlobalSoundS2CPacket(SoundEvent event, float volume, float pitch) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("global_sound_packet");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(Registries.SOUND_EVENT.getId(event));
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier soundId = buf.readIdentifier();
        float volume = buf.readFloat();
        float pitch = buf.readFloat();

        client.execute(() -> {
            SoundEvent sound = Registries.SOUND_EVENT.get(soundId);
            if(sound == null) return;

            client.getSoundManager().play(
                    PositionedSoundInstance.master(
                            sound,
                            pitch,
                            volume
                    )
            );
        });
    }
}
