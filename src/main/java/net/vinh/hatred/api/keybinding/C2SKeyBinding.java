package net.vinh.hatred.api.keybinding;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class C2SKeyBinding extends KeyBinding  implements C2SPacketHandler {
    public C2SKeyBinding(String translationKey, int code, String category) {
        this(translationKey, InputUtil.Type.KEYSYM, code, category);
    }

    public C2SKeyBinding(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
    }
}