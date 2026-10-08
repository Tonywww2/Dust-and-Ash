package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.platform.PlatformIdentifier;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.CentrifugeContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import com.tonywww.dustandash.platform.PlatformContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CentrifugeScreen extends PlatformContainerScreen<CentrifugeContainerMenu> {

    private final ResourceLocation GUI = PlatformIdentifier.id(DustAndAsh.MOD_ID, "textures/gui/centrifuge_gui.png");

    public CentrifugeScreen(CentrifugeContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackdrop(guiGraphics, pMouseX, pMouseY, pPartialTicks);
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
        int s = (int) (39 * r);
        // Render progressing bar
        guiGraphics.blit(GUI, i + 99, j + 5, 61, 172, s, 3);

        guiGraphics.blit(GUI, i + 77 - s, j + 5, 39 - s, 172, s, 3);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {

    }
}
