package com.tonywww.dustandash.cthulhu.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class CthulhuHintOverlay implements IGuiOverlay {

    public static final CthulhuHintOverlay INSTANCE = new CthulhuHintOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();

    private CthulhuHintOverlay() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        List<ClientHintManager.HintEntry> hints = ClientHintManager.activeHints();
        if (hints.isEmpty()) {
            return;
        }

        Font font = minecraft.font;
        long now = System.currentTimeMillis();
        int y = screenHeight / 4;
        for (ClientHintManager.HintEntry hint : hints) {
            int alpha = Math.min(255, Math.max(0, (int) (hint.alpha(now) * 255.0f)));
            int color = (alpha << 24) | 0x00F2F2F2;
            int x = (screenWidth - font.width(hint.component())) / 2;
            guiGraphics.drawString(font, hint.component(), x, y, color, true);
            y += 14;
        }
    }
}
