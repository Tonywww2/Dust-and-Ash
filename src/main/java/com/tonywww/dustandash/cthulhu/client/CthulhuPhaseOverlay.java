package com.tonywww.dustandash.cthulhu.client;

import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Optional;

public class CthulhuPhaseOverlay implements IGuiOverlay {

    public static final CthulhuPhaseOverlay INSTANCE = new CthulhuPhaseOverlay();

    private CthulhuPhaseOverlay() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Optional<FightPhase> phase = ClientCthulhuPhaseState.phase();
        if (phase.isEmpty() || Minecraft.getInstance().player == null) {
            return;
        }

        switch (phase.get()) {
            case PHASE_1 -> renderColorDrain(guiGraphics, screenWidth, screenHeight);
            case PHASE_2 -> renderMonochromeNoise(guiGraphics, screenWidth, screenHeight);
            case PHASE_3, FINAL_TRUTH -> renderGrid(guiGraphics, screenWidth, screenHeight);
            case TERMINATED -> {
            }
        }
    }

    private static void renderColorDrain(GuiGraphics guiGraphics, int width, int height) {
        guiGraphics.fill(0, 0, width, height, 0x38000000);
        guiGraphics.fill(0, 0, width, height, 0x14202020);
    }

    private static void renderMonochromeNoise(GuiGraphics guiGraphics, int width, int height) {
        guiGraphics.fill(0, 0, width, height, 0x70000000);
        int time = Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount;
        for (int y = 0; y < height; y += 4) {
            int alpha = ((y + time) % 12 == 0) ? 0x2A : 0x10;
            guiGraphics.fill(0, y, width, y + 1, (alpha << 24) | 0x00FFFFFF);
        }
    }

    private static void renderGrid(GuiGraphics guiGraphics, int width, int height) {
        guiGraphics.fill(0, 0, width, height, 0xD8000000);
        int spacing = 16;
        int offset = Minecraft.getInstance().player == null ? 0 : Minecraft.getInstance().player.tickCount % spacing;
        for (int x = -offset; x < width; x += spacing) {
            guiGraphics.fill(x, 0, x + 1, height, 0x55FFFFFF);
        }
        for (int y = offset; y < height; y += spacing) {
            guiGraphics.fill(0, y, width, y + 1, 0x55FFFFFF);
        }
        guiGraphics.fill(width / 2 - 1, 0, width / 2 + 1, height, 0x77FFFFFF);
        guiGraphics.fill(0, height / 2 - 1, width, height / 2 + 1, 0x77FFFFFF);
    }
}
