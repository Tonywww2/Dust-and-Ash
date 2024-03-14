package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.CentrifugeContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TextComponent;

public class CentrifugeScreen extends AbstractContainerScreen<CentrifugeContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/centrifuge_gui.png");

    public CentrifugeScreen(CentrifugeContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);

    }

    @Override
    protected void renderBg(PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);
        int i = this.leftPos;
        int j = this.topPos;

        this.blit(pMatrixStack, i, j, 0, 0, 175, 172);

        float r = this.menu.getProgressionRatio();
        int s = (int) (39 * r);
        // Render progressing bar
        this.blit(pMatrixStack, i + 99, j + 5, 61, 172, s, 3);

        this.blit(pMatrixStack, i + 77 - s, j + 5, 39 - s, 172, s, 3);

    }

    @Override
    protected void renderLabels(PoseStack pMatrixStack, int pX, int pY) {

    }
}
