package net.vinh.hatred.client.animation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnimationController {

    private final List<AnimationInstance> active = new ArrayList<>();

    public void play(AnimationDefinition definition) {
        active.add(new AnimationInstance(definition));
        active.sort(Comparator.comparingInt(
                a -> -a.getDefinition().priority()));
    }

    public void tick() {
        active.removeIf(instance -> {
            instance.tick();
            return instance.isFinished();
        });
    }

    public BodyTransform computeBone(String bone) {

        BodyTransform result = BodyTransform.identity();

        for (AnimationInstance instance : active) {

            KeyframeTrack track =
                    instance.getDefinition()
                            .tracks()
                            .get(bone);

            if (track == null) continue;

            BodyTransform sampled =
                    track.sample(instance.progress());

            if (instance.getDefinition()
                    .blendMode() == AnimationBlendMode.OVERRIDE) {
                result = sampled;
            } else {
                result = add(result, sampled);
            }
        }

        return result;
    }

    public BodyTransform computeRoot() {
        BodyTransform result = BodyTransform.identity();

        for (AnimationInstance instance : active) {
            if (instance == null) continue;
            if (instance.getDefinition() == null) continue;
            if (instance.getDefinition().tracks() == null) continue;

            KeyframeTrack track =
                    instance.getDefinition().tracks().get("root");

            if (track == null) continue;

            BodyTransform sampled =
                    track.sample(instance.progress());

            if (instance.getDefinition().blendMode() == AnimationBlendMode.OVERRIDE) {
                result = sampled;
            } else {
                result = add(result, sampled);
            }
        }

        return result;
    }

    private BodyTransform add(BodyTransform a, BodyTransform b) {
        return new BodyTransform(
                a.pitch() + b.pitch(),
                a.yaw() + b.yaw(),
                a.roll() + b.roll(),
                a.x() + b.x(),
                a.y() + b.y(),
                a.z() + b.z(),
                a.scale() + b.scale()
        );
    }
}
