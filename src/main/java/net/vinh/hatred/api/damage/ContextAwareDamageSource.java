package net.vinh.hatred.api.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class ContextAwareDamageSource extends DamageSource {
    private final DamageContext context;

    public ContextAwareDamageSource(RegistryEntry<DamageType> type, DamageContext context) {
        super(type, context.directSource(), context.attacker());
        this.context = context;
    }

    public DamageContext context() {
        return context;
    }

    @Override
    public Text getDeathMessage(LivingEntity killed) {
        if(context.deathMessage() != null) {
            if(context.addKilledDisplayNameToMsg()) {
                return Text.literal(killed.getDisplayName().getString() + " " + context.deathMessage().getString());
            } else {
                return context.deathMessage();
            }
        }

        return super.getDeathMessage(killed);
    }
}
