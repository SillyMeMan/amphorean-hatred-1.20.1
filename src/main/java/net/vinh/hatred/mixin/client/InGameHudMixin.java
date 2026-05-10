package net.vinh.hatred.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.vinh.hatred.api.client.screen.ClientHudState;
import net.vinh.hatred.api.client.screen.HudTextEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void hatred$render(DrawContext drawContext, float tickDelta, CallbackInfo ci) {
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
