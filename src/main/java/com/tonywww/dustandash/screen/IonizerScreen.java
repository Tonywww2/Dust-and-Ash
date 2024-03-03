package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.container.IonizerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class IonizerScreen extends ContainerScreen<IonizerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/ionizer_gui.png");

    public IonizerScreen(IonizerContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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

        this.blit(pMatrixStack, i, j, 0, 0, 175, 172);

        float r = this.menu.getProgressionRatio();
        int s = (int) (30 * r);
        // Render progressing bar
        this.blit(pMatrixStack, i + 74, j + 28, 88, 172, s, 9);

        // left
        if (this.menu.getTileEntity().invItemStackHandler.getStackInSlot(4).getCount() > 0) {
            this.blit(pMatrixStack, i + 61, j + 40, 84, 172, 2, 16);

        }

        // right
        if (this.menu.getTileEntity().invItemStackHandler.getStackInSlot(5).getCount() > 0) {
            this.blit(pMatrixStack, i + 113, j + 40, 86, 172, 2, 16);

        }

        //below
        if (this.menu.getBelowAvailable()) {
            this.blit(pMatrixStack, i + 46, j + 48, 0, 172, 83, 40);

        }

    }

    @Override
    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {

    }
}
