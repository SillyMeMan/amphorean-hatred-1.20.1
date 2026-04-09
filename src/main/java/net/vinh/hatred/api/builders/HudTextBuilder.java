package net.vinh.hatred.api.builders;

import net.minecraft.text.Text;
import net.vinh.hatred.api.client.screen.HudTextEntry;
import net.vinh.hatred.api.misc.AbstractBuilder;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class HudTextBuilder extends AbstractBuilder<HudTextEntry> {
    private Text text;
    private int priority;

    private int duration;

    private int color;
    private boolean shadow;

    public HudTextBuilder text(Text text) {
        this.text = text;
        return this;
    }

    public HudTextBuilder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public HudTextBuilder duration(int durationInTicks) {
        this.duration = durationInTicks;
        return this;
    }

    public HudTextBuilder color(int color) {
        this.color = color;
        return this;
    }

    public HudTextBuilder shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    @Override
    public HudTextEntry build() {
        return new HudTextEntry(text, priority, duration, color, shadow);
    }
}
