package net.vinh.hatred.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.vinh.hatred.api.keybinding.C2SKeyBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class C2SKeyBindingHelper {
    private static final List<C2SKeyBinding> BINDINGS = new ArrayList<>();

    private C2SKeyBindingHelper() {}

    public static <T extends C2SKeyBinding> T register(T binding) {
        if(BINDINGS.stream().anyMatch(c2SKeyBinding -> c2SKeyBinding.packetId().equals(c2SKeyBinding.packetId()))) throw new IllegalArgumentException(
                "Duplicate C2S keybinding packet ID: " + binding.packetId()
        );

        BINDINGS.add(binding);
        return binding;
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        for (C2SKeyBinding binding : BINDINGS) {
            KeyBindingHelper.registerKeyBinding(binding);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (C2SKeyBinding binding : BINDINGS) {
                while (binding.wasPressed()) {
                    PacketByteBuf buf = PacketByteBufs.empty();
                    binding.writeBuf(buf);

                    ClientPlayNetworking.send(binding.packetId(), buf);
                }
            }
        });
    }

    public static List<C2SKeyBinding> getAll() {
        return Collections.unmodifiableList(BINDINGS);
    }
}
