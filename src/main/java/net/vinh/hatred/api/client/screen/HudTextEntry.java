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

    public static class HudTextRenderer implements HudRenderCallback {
        @Override
        public void onHudRender(DrawContext drawContext, float v) {
            MinecraftClient client = MinecraftClient.getInstance();

            HudTextEntry entry = ClientHudState.get();

            if (entry == null) return;

            TextRenderer tr = client.textRenderer;

            int textWidth = tr.getWidth(entry.text);
            int x = (client.getWindow().getScaledWidth() - textWidth) / 2;
            int y = client.getWindow().getScaledHeight() / 2 - 90;

            int left = x - 26;
            int right = x + textWidth + 24;
            int top = y - 8;
            int bottom = y + tr.fontHeight + 6;

            int bgColor = 0xFF0000FF;

            drawContext.drawBorder(x, y, textWidth + 24, tr.fontHeight + 6, entry.color);
            drawContext.fill(left, top, right, bottom, bgColor);

            drawContext.drawText(
                    tr,
                    entry.text,
                    x,
                    y,
                    entry.color,
                    entry.shadow
            );
        }
    }
}
