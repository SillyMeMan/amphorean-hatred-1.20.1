package net.vinh.hatred.client.animation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record KeyframeTrack(List<Keyframe> keyframes) {
    public KeyframeTrack {
        if (keyframes == null || keyframes.isEmpty()) {
            throw new IllegalArgumentException(
                    "A keyframe track must contain at least one keyframe"
            );
        }

        keyframes = new ArrayList<>(keyframes);
        keyframes.sort(Comparator.comparingDouble(Keyframe::time));
        keyframes = List.copyOf(keyframes);
    }

    public BodyTransform sample(float progress) {
        if (!Float.isFinite(progress)) {
            throw new IllegalArgumentException(
                    "Animation progress must be finite"
            );
        }

        if (keyframes.size() == 1) {
            return keyframes.get(0).transform();
        }

        Keyframe previous = keyframes.get(0);

        if (progress <= previous.time()) {
            return previous.transform();
        }

        for (int i = 1; i < keyframes.size(); i++) {
            Keyframe next = keyframes.get(i);

            if (progress <= next.time()) {
                float duration = next.time() - previous.time();

                if (duration <= 0.0f) {
                    return next.transform();
                }

                float delta = (progress - previous.time()) / duration;

                return BodyTransform.interpolate(
                        previous.transform(),
                        next.transform(),
                        delta
                );
            }

            previous = next;
        }

        return previous.transform();
    }
}
