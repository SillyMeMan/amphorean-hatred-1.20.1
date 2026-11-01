package net.vinh.hatred.client.animation;

import net.minecraft.util.Identifier;
import net.vinh.hatred.api.registry.HatredRegistries;

import java.util.*;

public final class AnimationController {
    private final List<AnimationInstance> active = new ArrayList<>();

    public void play(AnimationDefinition definition) {
        AnimationInstance instance = new AnimationInstance(definition);

        active.add(instance);
        active.sort(
                Comparator.comparingInt(
                        animation -> animation.definition().priority()
                )
        );
    }

    public void stop(AnimationInstance instance) {
        active.remove(instance);
    }

    public void stop(Identifier animationId) {
        active.removeIf(instance ->
                Objects.equals(
                        HatredRegistries.ANIMATION.getId(instance.definition()),
                        animationId
                )
        );
    }

    public void stopAll() {
        active.clear();
    }

    public void tick() {
        active.removeIf(AnimationInstance::isFinished);

        for (AnimationInstance instance : active) {
            instance.tick();
        }
    }

    public BodyTransform computeBone(String bone, float tickDelta) {
        BodyTransform result = BodyTransform.IDENTITY;

        for (AnimationInstance instance : active) {
            BodyTransform sampled = instance.sample(bone, tickDelta);

            if (sampled == null) {
                continue;
            }

            if (instance.definition().blendMode() == AnimationBlendMode.OVERRIDE) {
                return sampled;
            }

            result = result.add(sampled);
        }

        return result;
    }

    public BodyTransform computeRoot(float tickDelta) {
        return computeBone("root", tickDelta);
    }

    public Set<String> getAnimatedJoints() {
        Set<String> joints = new HashSet<>();

        for (AnimationInstance instance : active) {
            joints.addAll(instance.definition().tracks().keySet());
        }

        return joints;
    }

    public List<AnimationInstance> getActiveAnimations() {
        return List.copyOf(active);
    }

    public boolean isPlaying() {
        return !active.isEmpty();
    }
}
