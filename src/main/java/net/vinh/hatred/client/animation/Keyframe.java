package net.vinh.hatred.client.animation;

public record Keyframe(
        float time,          // 0.0 → 1.0
        BodyTransform transform
) {}
