package net.vinh.hatred.api.damage;

@FunctionalInterface
public interface DamageDistributor {
    float distribute(float totalDamage);
}
