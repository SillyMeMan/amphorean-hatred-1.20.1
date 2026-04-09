package net.vinh.hatred.client.animation;

import java.util.List;

public class KeyframeTrack {
    private final List<Keyframe> keyframes;

    public KeyframeTrack(List<Keyframe> keyframes) {
        this.keyframes = keyframes;
    }

    public BodyTransform sample(float progress) {
        if (keyframes.isEmpty())
            return BodyTransform.identity();

        if (keyframes.size() == 1)
            return keyframes.get(0).transform();

        Keyframe previous = keyframes.get(0);
        Keyframe next = keyframes.get(keyframes.size() - 1);

        for (Keyframe k : keyframes) {
            if (k.time() >= progress) {
                next = k;
                break;
            }
            previous = k;
        }

        float timeDiff = next.time() - previous.time();

        if (timeDiff == 0f)
            return previous.transform();

        float delta = (progress - previous.time()) / timeDiff;

        return BodyTransform.interpolate(
                previous.transform(),
                next.transform(),
                delta
        );
    }
}
