package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.menu.AutomaticMillingRecyclerMenu;
import com.tonywww.dustandash.platform.PlatformContainerScreen;
import com.tonywww.dustandash.platform.PlatformIdentifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutomaticMillingRecyclerScreen extends PlatformContainerScreen<AutomaticMillingRecyclerMenu> {
    private static final ResourceLocation GUI = PlatformIdentifier.id("dustandash", "textures/gui/automatic_milling_recycler.png");
    private final Button[] cells = new Button[25];
    private final boolean[] displayed = new boolean[25];

    public AutomaticMillingRecyclerScreen(AutomaticMillingRecyclerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 227;
        inventoryLabelY = 134;
    }

    @Override
    protected void init() {
        super.init();
        for (int cell = 0; cell < cells.length; cell++) {
            cells[cell] = addRenderableWidget(new GridButton(cell));
            updateCell(cell);
        }
    }

    private void toggleCell(int cell) {
        if (minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, cell);
        }
    }

    private void updateCell(int cell) {
        displayed[cell] = menu.isSlotEnabled(cell);
        Component state = Component.translatable(displayed[cell]
                ? "screen.dustandash.recycler.keep" : "screen.dustandash.recycler.collect");
        cells[cell].setMessage(Component.translatable("screen.dustandash.recycler.cell", cell / 5 + 1, cell % 5 + 1, state));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        for (int cell = 0; cell < cells.length; cell++) {
            if (displayed[cell] != menu.isSlotEnabled(cell)) updateCell(cell);
        }
        renderBackdrop(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawWordWrap(font, Component.translatable("screen.dustandash.recycler.legend"), 81, 24, 87, 0x404040);
    }

    /** Compact symbols, with full localized text reserved for keyboard narration. */
    private final class GridButton extends Button {
        private final int cell;

        private GridButton(int cell) {
            super(leftPos + 8 + (cell % 5) * 13, topPos + 20 + (cell / 5) * 13, 12, 12,
                    Component.empty(), button -> toggleCell(cell), DEFAULT_NARRATION);
            this.cell = cell;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int x = getX(), y = getY();
            boolean keep = displayed[cell];
            graphics.fill(x, y, x + 12, y + 12, 0xFF222A27);
            graphics.fill(x + 1, y + 1, x + 11, y + 11, keep ? 0xFF365F47 : 0xFF70483B);
            graphics.fill(x + 3, y + 5, x + 9, y + 7, keep ? 0xFFB3D69C : 0xFFF1B371);
            if (keep) graphics.fill(x + 5, y + 3, x + 7, y + 9, 0xFFB3D69C);
        }
    }
}
