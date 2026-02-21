package net.vinh.hatred.attachment;

import net.minecraft.util.Identifier;
import net.vinh.hatred.AmphoreanHatred;
import net.vinh.hatred.api.data.DataAttachmentType;
import net.vinh.hatred.api.data.DataRegistry;
import net.vinh.hatred.api.scheduler.EntityScheduler;
import net.vinh.hatred.api.scheduler.WorldScheduler;

public class HatredDataAttachmentTypes {
    public static final DataAttachmentType<WorldScheduler> WORLD_SCHEDULER = DataRegistry.register(new Identifier(AmphoreanHatred.MOD_ID, "world_scheduler"), WorldScheduler.class, WorldScheduler::new, null, false, false);
    public static final DataAttachmentType<EntityScheduler> ENTITY_SCHEDULER = DataRegistry.register(new Identifier(AmphoreanHatred.MOD_ID, "entity_scheduler"), EntityScheduler.class, EntityScheduler::new, null, false, false);
}
