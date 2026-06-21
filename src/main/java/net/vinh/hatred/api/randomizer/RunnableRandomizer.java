package net.vinh.hatred.api.randomizer;

import net.minecraft.util.math.random.Random;

public class RunnableRandomizer extends Randomizer<Runnable> {
    public RunnableRandomizer(Runnable[] runnables) {
        super(runnables);
    }

    public RunnableRandomizer selectAndForEach(int amount, Random rnd) {
        return (RunnableRandomizer) super.selectAndForEach(amount, rnd, Runnable::run);
    }
}
