package net.vinh.hatred.api.client.screen;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class HudTextEntry {
    public final Text text;
    public final int priority;

    public int ticksRemaining;

    public final int color;
    public final boolean shadow;

    public HudTextEntry(Text text, int priority, int durationTicks, int color, boolean shadow) {
        this.text = text;
        this.priority = priority;
        this.ticksRemaining = durationTicks;
        this.color = color;
        this.shadow = shadow;
    }

    public void tick() {
        ticksRemaining--;
    }

    public boolean isExpired() {
        return ticksRemaining <= 0;
    }
}
