package net.vinh.hatred.client.animation;

import java.util.Map;
import java.util.Objects;

public record AnimationDefinition(
        int duration,
        Map<String, KeyframeTrack> tracks,
        AnimationBlendMode blendMode,
        int priority
) {
    public AnimationDefinition {
        if (duration <= 0) {
            throw new IllegalArgumentException(
                    "Animation duration must be greater than zero"
            );
        }

        Objects.requireNonNull(tracks, "Animation tracks cannot be null");
        Objects.requireNonNull(blendMode, "Animation blend mode cannot be null");

        if (tracks.isEmpty()) {
            throw new IllegalArgumentException(
                    "Animation must contain at least one track"
            );
        }

        tracks = Map.copyOf(tracks);

        tracks.forEach((bone, track) -> {
            if (bone == null || bone.isBlank()) {
                throw new IllegalArgumentException(
                        "Animation track names cannot be blank"
                );
            }

            Objects.requireNonNull(
                    track,
                    "Animation track cannot be null: " + bone
            );
        });

        if (priority < 0) {
            throw new IllegalArgumentException(
                    "Animation priority cannot be negative"
            );
        }
    }

    public KeyframeTrack getTrack(String bone) {
        return tracks.get(bone);
    }
}
