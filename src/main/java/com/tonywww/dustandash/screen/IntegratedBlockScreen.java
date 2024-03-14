package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.IntegratedBlockContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class IntegratedBlockScreen extends AbstractContainerScreen<IntegratedBlockContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/integrated_block_gui.png");

    public IntegratedBlockScreen(IntegratedBlockContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
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
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight + 2);

        int level = menu.getStructureLevel();
        // structure checker

        if (menu.isBeaconOn()) {
            this.blit(pMatrixStack, i + 57, j + 3, 176, 46, 62, 162);
        }

        if (level > 0) {
            // top left
            int s = 176;
            this.blit(pMatrixStack, i + 54, j + 27, s, 0, 9, 9);
            // top right
            this.blit(pMatrixStack, i + 113, j + 27, s + 3, 0, 9, 9);
            // bot left
            this.blit(pMatrixStack, i + 54, j + 64, s, 3, 9, 9);
            // bot right
            this.blit(pMatrixStack, i + 113, j + 64, s + 3, 3, 9, 9);

            if (level > 1) {
                this.blit(pMatrixStack, i + 61, j + 27, 188, 0, 54, 46);

                if (level > 2) {
                    this.blit(pMatrixStack, i + 32, j + 33, 0, 168, 112, 34);

                }
            }


        }

    }
}
