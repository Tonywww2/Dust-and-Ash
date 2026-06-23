package com.tonywww.dustandash.cthulhu.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CthulhuGraphemeOverlay implements IGuiOverlay {

    public static final CthulhuGraphemeOverlay INSTANCE = new CthulhuGraphemeOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();

    private CthulhuGraphemeOverlay() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (minecraft.player == null || minecraft.player.isSpectator() || ClientGraphemeData.isEmpty()) {
            return;
        }

        List<Character> letters = expandLetters(ClientGraphemeData.letters());
        if (letters.isEmpty()) {
            return;
        }

        Font font = minecraft.font;
        double time = (minecraft.player.tickCount + partialTick) * 0.03d;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        int radiusX = 72;
        int radiusY = 28;

        for (int i = 0; i < letters.size(); i++) {
            double angle = time + (Math.PI * 2.0d * i / letters.size());
            String text = String.valueOf(letters.get(i));
            int x = centerX + (int) Math.round(Math.cos(angle) * radiusX) - font.width(text) / 2;
            int y = centerY + (int) Math.round(Math.sin(angle) * radiusY) - 4;
            guiGraphics.drawString(font, text, x, y, 0xFFFFFFFF, true);
        }
    }

    private static List<Character> expandLetters(Map<Character, Integer> letters) {
        List<Character> expanded = new ArrayList<>();
        letters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    int count = Math.min(entry.getValue(), 8);
                    for (int i = 0; i < count; i++) {
                        expanded.add(entry.getKey());
                    }
                });
        return expanded;
    }
}
