package net.vinh.hatred.api.damage;

import net.minecraft.util.math.random.Random;

public final class DamageDistributors {
    public static final DamageDistributor FULL_DAMAGE = totalDamage -> totalDamage;

    public static DamageDistributor evenDistribution(int targetCount) {
        return totalDamage -> totalDamage / targetCount;
    }

    public static DamageDistributor randomBonus(double chance, float bonusMultiplier, float failedMultiplier) {
        if(chance > 1 || chance < 0) throw new IllegalArgumentException("Chance must be smaller or equal to 1 and non-negative");

        return totalDamage -> {
            Random random = Random.createLocal();

            if(random.nextDouble() < chance) {
                return totalDamage * bonusMultiplier;
            }

            return totalDamage * failedMultiplier;
        };
    }

    private DamageDistributors() {
        throw new AssertionError("Class is not supposed to be instantized");
    }
}
