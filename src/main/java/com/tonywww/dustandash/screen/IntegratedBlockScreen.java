package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.container.IntegratedBlockContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class IntegratedBlockScreen extends ContainerScreen<IntegratedBlockContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/integrated_block_gui.png");

    public IntegratedBlockScreen(IntegratedBlockContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);

    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);

        if (this.minecraft == null) return;

        this.minecraft.getTextureManager().bind(GUI);
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
