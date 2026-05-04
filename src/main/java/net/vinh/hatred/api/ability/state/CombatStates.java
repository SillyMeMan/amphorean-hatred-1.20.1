package net.vinh.hatred.api.ability.state;

import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataRegistry;

public class CombatStates {
    public static final DataAttachmentType<Boolean> MOVEMENT_FROZEN = DataRegistry.registerBoolean(AmphoreanHatred.id("movement_frozen"), () -> false, false, true);
    public static final DataAttachmentType<Boolean> INVENTORY_FROZEN = DataRegistry.registerBoolean(AmphoreanHatred.id("inventory_frozen"), () -> false, false, true);
    public static final DataAttachmentType<Boolean> ROTATION_LOCKED = DataRegistry.registerBoolean(AmphoreanHatred.id("rotation_locked"), () -> false, false, true);
}
