package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.container.IntegratedBlockContainer;
import com.tonywww.dustandash.container.MillingMachineContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MillingMachineScreen extends ContainerScreen<MillingMachineContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/milling_machine_gui.png");

    public MillingMachineScreen(MillingMachineContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
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

        this.blit(pMatrixStack, i, j, 0, 0, 175, 173);

        if (!this.menu.isWorkSpaceEmpty()) {
            // Render workspace
            this.blit(pMatrixStack, i + 71, j + 3, 0, 173, 97, 82);

        }

    }

    @Override
    protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {

    }
}
