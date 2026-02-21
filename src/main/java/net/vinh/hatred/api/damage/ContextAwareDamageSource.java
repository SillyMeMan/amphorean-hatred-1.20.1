package net.vinh.hatred.api.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;

public class ContextAwareDamageSource extends DamageSource {
    private final DamageContext context;

    public ContextAwareDamageSource(RegistryEntry<DamageType> type, DamageContext context) {
        super(type, context.attacker());
        this.context = context;
    }

    public DamageContext context() {
        return context;
    }
}
