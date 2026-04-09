package net.vinh.hatred.client.animation;

import java.util.Map;

public record AnimationDefinition(int duration, Map<String, KeyframeTrack> tracks, AnimationBlendMode blendMode, int priority) {
}
