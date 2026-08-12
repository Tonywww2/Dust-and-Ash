package com.tonywww.dustandash.overlay;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.client.cooldown.CurioCooldownOverlayApi;
import com.tonywww.dustandash.client.cooldown.CurioCooldownOverlayApi.VisibleCooldown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public final class CurioCooldownOverlay implements IGuiOverlay {
    public static final CurioCooldownOverlay INSTANCE = new CurioCooldownOverlay();

    private static final int ICON_SIZE = 16;
    private static final int BAR_WIDTH = 68;
    private static final int BAR_HEIGHT = 6;
    private static final int ROW_HEIGHT = 20;

    private CurioCooldownOverlay() {
    }

    @Override
    public void render(
            ForgeGui gui,
            GuiGraphics graphics,
            float partialTick,
            int screenWidth,
            int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        List<VisibleCooldown> cooldowns = CurioCooldownOverlayApi.visibleCooldowns();
        int x = DustAndAshConfig.CLIENT.cooldownOverlayX.get();
        int y = DustAndAshConfig.CLIENT.cooldownOverlayY.get();
        for (VisibleCooldown cooldown : cooldowns) {
            ItemStack icon = new ItemStack(cooldown.definition().item().get());
            graphics.renderItem(icon, x, y);

            int barX = x + ICON_SIZE + 4;
            int barY = y + (ICON_SIZE - BAR_HEIGHT) / 2;
            graphics.fill(
                    barX - 1,
                    barY - 1,
                    barX + BAR_WIDTH + 1,
                    barY + BAR_HEIGHT + 1,
                    0xB0FFFFFF);
            graphics.fill(
                    barX,
                    barY,
                    barX + BAR_WIDTH,
                    barY + BAR_HEIGHT,
                    cooldown.definition().trackColor());
            int fillWidth = Math.round(BAR_WIDTH * cooldown.progress());
            if (fillWidth > 0) {
                graphics.fill(
                        barX,
                        barY,
                        barX + fillWidth,
                        barY + BAR_HEIGHT,
                        cooldown.definition().fillColor());
            }
            y += ROW_HEIGHT;
        }
    }
}