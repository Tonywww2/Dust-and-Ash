package com.tonywww.dustandash.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.MillingMachineContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MillingMachineScreen extends AbstractContainerScreen<MillingMachineContainer> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/milling_machine_gui.png");

    public MillingMachineScreen(MillingMachineContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
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

        this.blit(pMatrixStack, i, j, 0, 0, 175, 173);

        if (!this.menu.isWorkSpaceEmpty()) {
            // Render workspace
            this.blit(pMatrixStack, i + 71, j + 3, 0, 173, 97, 82);

        }

    }

    @Override
    protected void renderLabels(PoseStack pMatrixStack, int pX, int pY) {

    }
}
