package com.tonywww.dustandash.client.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public final class ClientNeutronTooltipComponent implements ClientTooltipComponent {
    private static final int MINIMUM_WIDTH = 96;
    private static final int TEXT_HEIGHT = 9;
    private static final int TEXT_TO_BAR_GAP = 2;
    private static final int BAR_HEIGHT = 7;
    private static final int BORDER_COLOR = 0xFF58727B;
    private static final int TRACK_COLOR = 0xFF10191D;
    private static final int FILL_TOP_COLOR = 0xFF7DEAFF;
    private static final int FILL_BOTTOM_COLOR = 0xFF247E9D;
    private static final int SEGMENT_COLOR = 0x8058727B;
    private static final int TEXT_COLOR = 0xFFE6F7FA;

    private final NeutronTooltipComponent component;
    private final Component label;

    public ClientNeutronTooltipComponent(NeutronTooltipComponent component) {
        this.component = component;
        this.label = Component.translatable(
                "tooltip.dustandash.neutron_amount",
                Component.literal(Integer.toString(component.neutron())).withStyle(ChatFormatting.AQUA),
                Component.literal(Integer.toString(component.maximum())).withStyle(ChatFormatting.DARK_AQUA));
    }

    @Override
    public int getHeight() {
        return TEXT_HEIGHT + TEXT_TO_BAR_GAP + BAR_HEIGHT;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(MINIMUM_WIDTH, font.width(this.label));
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int width = this.getWidth(font);
        graphics.drawString(font, this.label, x, y, TEXT_COLOR, false);

        int barY = y + TEXT_HEIGHT + TEXT_TO_BAR_GAP;
        graphics.fill(x, barY, x + width, barY + BAR_HEIGHT, BORDER_COLOR);
        graphics.fill(x + 1, barY + 1, x + width - 1, barY + BAR_HEIGHT - 1, TRACK_COLOR);

        int innerWidth = width - 2;
        int fillWidth = Math.round(innerWidth * this.component.progress());
        if (fillWidth > 0) {
            graphics.fillGradient(
                    x + 1,
                    barY + 1,
                    x + 1 + fillWidth,
                    barY + BAR_HEIGHT - 1,
                    FILL_TOP_COLOR,
                    FILL_BOTTOM_COLOR);
        }

        for (int segment = 1; segment < 4; segment++) {
            int segmentX = x + 1 + Math.round(innerWidth * segment / 4f);
            graphics.fill(segmentX, barY + 1, segmentX + 1, barY + BAR_HEIGHT - 1, SEGMENT_COLOR);
        }
    }
}