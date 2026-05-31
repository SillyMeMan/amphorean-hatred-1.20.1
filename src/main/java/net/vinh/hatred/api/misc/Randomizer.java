package net.vinh.hatred.api.misc;

import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Randomizer<T> {
    private final List<T> OBJECTS;

    public Randomizer(T[] objects) {
        this.OBJECTS = new ArrayList<>(List.of(objects));
    }

    @SafeVarargs
    public final Randomizer<T> addMultiple(T... objects) {
        this.OBJECTS.addAll(Arrays.asList(objects));
        return this;
    }

    public Randomizer<T> add(T object) {
        return addMultiple(object);
    }

    public Randomizer<T> selectAndForEach(int amount, Random rnd, Consumer<T> consumer) {
        if (this.OBJECTS.isEmpty()) return this;

        for (int i = 0; i < amount; i++) {
            Collections.shuffle(this.OBJECTS);
            T object = this.OBJECTS.get(rnd.nextInt(this.OBJECTS.size()));
            consumer.accept(object);
        }

        return this;
    }
}
