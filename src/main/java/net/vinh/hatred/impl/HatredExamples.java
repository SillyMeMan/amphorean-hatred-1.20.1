package net.vinh.hatred.impl;

import net.minecraft.registry.Registry;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.registry.AutoInitializer;
import net.vinh.hatred.api.registry.HatredRegistries;
import net.vinh.hatred.client.animation.*;

import java.util.List;
import java.util.Map;

@AutoInitializer
public class HatredExamples {
    public static final AnimationDefinition TEST_ANIMATION;

    static {
        TEST_ANIMATION = Registry.register(HatredRegistries.ANIMATION, AmphoreanHatred.id("test_anim"), new AnimationDefinition(80, Map.of(
                "right_arm",
                new KeyframeTrack(List.of(
                        new Keyframe(0f, BodyTransform.IDENTITY),
                        new Keyframe(0.25f, new BodyTransform(180, 0, 0, 0, 0, 0, 1)),
                        new Keyframe(0.50f, BodyTransform.IDENTITY),
                        new Keyframe(0.75f, new BodyTransform(180, 0, 0, 0, 0, 0, 1)),
                        new Keyframe(1f, BodyTransform.IDENTITY)
                )),
                "left_arm",
                new KeyframeTrack(List.of(
                        new Keyframe(0f, BodyTransform.IDENTITY),
                        new Keyframe(0.25f, new BodyTransform(180, 0, 0, 0, 0, 0, 1)),
                        new Keyframe(0.50f, BodyTransform.IDENTITY),
                        new Keyframe(0.75f, new BodyTransform(180, 0, 0, 0, 0, 0, 1)),
                        new Keyframe(1f, BodyTransform.IDENTITY)
                ))
        ), AnimationBlendMode.OVERRIDE, 0));
    }
}
