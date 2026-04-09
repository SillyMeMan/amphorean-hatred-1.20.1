package net.vinh.hatred.api.math;

import java.util.Random;

public final class RandomMath {
    private RandomMath() {}

    public static double randomBetween(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    public static int randomBetween(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static boolean chance(Random random, double probability) {
        return random.nextDouble() <= probability;
    }
}

