package net.vinh.hatred.api.randomizer;

import net.minecraft.util.math.random.Random;
import net.vinh.hatred.AmphoreanHatred;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class DualRandomizer<T, U> {
    private final List<T> FIRST;
    private final List<U> SECOND;

    public DualRandomizer(T[] first, U[] second) {
        this.FIRST = new ArrayList<>(List.of(first));
        this.SECOND = new ArrayList<>(List.of(second));
    }

    @SafeVarargs
    public final DualRandomizer<T, U> addMultipleFirst(T... objects) {
        this.FIRST.addAll(Arrays.asList(objects));
        return this;
    }

    public DualRandomizer<T, U> addFirst(T object) {
        return addMultipleFirst(object);
    }

    @SafeVarargs
    public final DualRandomizer<T, U> addMultipleSecond(U... objects) {
        this.SECOND.addAll(Arrays.asList(objects));
        return this;
    }

    public DualRandomizer<T, U> addSecond(U object) {
        return addMultipleSecond(object);
    }

    public DualRandomizer<T, U> selectAndForEach(int amountOfEach, Random rnd, BiConsumer<T, U> consumer) {
        if (this.FIRST.isEmpty() || this.SECOND.isEmpty()) {
            AmphoreanHatred.LOGGER.warn("One of two varargs is empty");
            return this;
        }

        for (int i = 0; i < amountOfEach; i++) {
            Collections.shuffle(this.FIRST);
            Collections.shuffle(this.SECOND);

            T first = this.FIRST.get(rnd.nextInt(this.FIRST.size()));
            U second = this.SECOND.get(rnd.nextInt(this.SECOND.size()));
            consumer.accept(first, second);
        }

        return this;
    }
}
