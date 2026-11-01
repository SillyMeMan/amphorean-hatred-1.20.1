package net.vinh.hatred.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.vinh.hatred.client.animation.*;
import net.vinh.hatred.internal.AutoRegistry;
import net.vinh.hatred.networking.packet.CrashS2CPacket;
import net.vinh.hatred.networking.packet.ShutdownS2CPacket;
import net.vinh.hatred.networking.packet.*;
import net.vinh.hatred.util.C2SKeyBindingHelper;

public class AmphoreanHatredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoRegistry.autoClientBootstrap();

        C2SKeyBindingHelper.initializeClient();

        AnimationModelAdapters.<PlayerEntityModel<?>>register(PlayerEntityModel.class, (model, joint, transform) -> {
            ModelPart part = switch (joint) {
                case "head" -> model.head;
                case "hat" -> model.hat;
                case "body" -> model.body;
                case "right_arm" -> model.rightArm;
                case "left_arm" -> model.leftArm;
                case "right_leg" -> model.rightLeg;
                case "left_leg" -> model.leftLeg;
                case "jacket" -> model.jacket;
                case "right_sleeve" -> model.rightSleeve;
                case "left_sleeve" -> model.leftSleeve;
                case "right_pants" -> model.rightPants;
                case "left_pants" -> model.leftPants;
                default -> null;
            };

            if (part != null) {
                AnimationTransforms.apply(part, transform);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ScreenshakeS2CPacket.ID, ScreenshakeS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(SyncHudS2CPacket.ID, SyncHudS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(CrashS2CPacket.ID, CrashS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(ShutdownS2CPacket.ID, ShutdownS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(GlobalSoundS2CPacket.ID, GlobalSoundS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(AnimationS2CPacket.ID, AnimationS2CPacket::handle);
    }
}
