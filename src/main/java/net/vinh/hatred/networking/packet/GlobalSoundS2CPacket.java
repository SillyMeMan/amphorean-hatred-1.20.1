package net.vinh.hatred.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.vinh.hatred.AmphoreanHatred;

public record GlobalSoundS2CPacket(SoundEvent event, float volume, float pitch, SoundCategory soundCategory, boolean stop) implements Packet<ClientPlayPacketListener> {
    public static final Identifier ID = AmphoreanHatred.id("global_sound_packet");

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(Registries.SOUND_EVENT.getId(event));
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
        buf.writeEnumConstant(soundCategory);
        buf.writeBoolean(stop);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier soundId = buf.readIdentifier();
        float volume = buf.readFloat();
        float pitch = buf.readFloat();
        SoundCategory category = buf.readEnumConstant(SoundCategory.class);

        boolean stop = buf.readBoolean();

        if(stop) {
            client.execute(() -> client.getSoundManager().stopSounds(soundId, category));
        } else {
            client.execute(() -> {
                SoundEvent sound = Registries.SOUND_EVENT.get(soundId);
                if(sound == null) return;

                PositionedSoundInstance instance = new PositionedSoundInstance(soundId, category, volume, pitch, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, true);

                client.getSoundManager().play(instance);
            });
        }
    }
}
