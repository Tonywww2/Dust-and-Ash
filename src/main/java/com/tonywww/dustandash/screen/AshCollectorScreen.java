package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.container.AshCollectorContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AshCollectorScreen extends ContainerScreen<AshCollectorContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/ash_collector_gui.png");

    public AshCollectorScreen(AshCollectorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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

    }
}
