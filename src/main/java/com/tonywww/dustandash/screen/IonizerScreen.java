package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.IonizerContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class IonizerScreen extends AbstractContainerScreen<IonizerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/ionizer_gui.png");

    public IonizerScreen(IonizerContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTicks, int pX, int pY) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);
        int i = this.leftPos;
        int j = this.topPos;

        guiGraphics.blit(GUI, i, j, 0, 0, 175, 172);

        float r = this.menu.getProgressionRatio();
        int s = (int) (30 * r);
        // Render progressing bar
        guiGraphics.blit(GUI, i + 74, j + 28, 88, 172, s, 9);

        // left
        if (this.menu.getTileEntity().invItemStackHandler.getStackInSlot(4).getCount() > 0) {
            guiGraphics.blit(GUI, i + 61, j + 40, 84, 172, 2, 16);

        }

        // right
        if (this.menu.getTileEntity().invItemStackHandler.getStackInSlot(5).getCount() > 0) {
            guiGraphics.blit(GUI, i + 113, j + 40, 86, 172, 2, 16);

        }

        //below
        if (this.menu.getBelowAvailable()) {
            guiGraphics.blit(GUI, i + 46, j + 48, 0, 172, 83, 40);

        }

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {

    }
}
