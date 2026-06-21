package net.vinh.hatred.api.math;

import java.util.Random;

public final class RandomMath {
    private RandomMath() {}

    public static long randomBetween(Random random, long min, long max) {
        return min + random.nextLong() * (max - min);
    }

    public static float randomBetween(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static double randomBetween(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    public static int randomBetween(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static boolean chance(Random random, double probability) throws IllegalArgumentException {
        if(probability > 1 || probability < 0) throw new IllegalArgumentException("Probability must be smaller or equal to 1 and non-negative");
        return random.nextDouble() <= probability;
    }
}

