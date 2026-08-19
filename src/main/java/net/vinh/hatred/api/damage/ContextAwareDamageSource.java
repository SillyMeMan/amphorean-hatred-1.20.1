package net.vinh.hatred.api.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
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
                return Text.literal(killed.getDisplayName().getString() + context.deathMessage().getString());
            } else {
                return context.deathMessage();
            }
        }

        return super.getDeathMessage(killed);
    }

    @Override
    public boolean isIn(TagKey<DamageType> tag) {
        if(tag == DamageTypeTags.BYPASSES_INVULNERABILITY && context.bypassesInvulnerability()) return true;
        if(tag == DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS && context.alwaysDamageEnderDragons()) return true;
        if(tag == DamageTypeTags.BYPASSES_COOLDOWN && context.bypassesCooldown()) return true;
        if(tag == DamageTypeTags.BYPASSES_SHIELD && context.bypassesShield()) return true;

        return super.isIn(tag);
    }
}
