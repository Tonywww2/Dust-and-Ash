package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class FissionReactorControllerScreen extends AbstractContainerScreen<FissionReactorControllerContainerMenu> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/fission_reactor_controller_gui.png");

    public FissionReactorControllerScreen(FissionReactorControllerContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
        guiGraphics.blit(GUI, i, j, 0, 0, 197, 192);

        ContainerData data = this.menu.getData();

        double rHeat = (double) data.get(0) / FissionReactorControllerEntity.MAX_HEAT;
        guiGraphics.blit(GUI, i + 66, j + 4, 0, 193,
                (int) (127 * rHeat), 9);

        double rNeutron = (double) data.get(5) / FissionReactorControllerEntity.MAX_NEUTRON;
        guiGraphics.blit(GUI, i + 66, j + 16, 0, 202,
                (int) (127 * rNeutron), 9);

        double rEnergy = (double) data.get(2) / FissionReactorControllerEntity.MAX_ENERGY;
        guiGraphics.blit(GUI, i + 66, j + 28, 0, 211,
                (int) (127 * rEnergy), 9);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_heat"),
                5, 5, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(this.menu.getHeat() + "/" + FissionReactorControllerEntity.MAX_HEAT),
                67, 5, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_neutron"),
                5, 17, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(this.menu.getNeutron() + "/" + FissionReactorControllerEntity.MAX_NEUTRON),
                67, 17, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_energy"),
                5, 29, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(this.menu.getEnergy() + "/" + FissionReactorControllerEntity.MAX_ENERGY),
                67, 29, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_efficiency"),
                5, 41, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getEfficiency())),
                68, 41, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_fuel_cell_count"),
                5, 53, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getFuelCellCount())),
                68, 53, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_cooling_cell_count"),
                5, 65, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getCoolingCellCount())),
                68, 65, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_energy_rate"),
                5, 77, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getEGP())),
                68, 77, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_radius"),
                101, 41, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getRadius())),
                164, 41, 2550255, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_height"),
                101, 53, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(this.menu.getHeight())),
                164, 53, 2550255, false);

    }

}
