package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronContainerUpdater;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronSlotState;
import com.tonywww.dustandash.block.entity.FissionReactor.ReactorOperatingState;
import com.tonywww.dustandash.block.entity.FissionReactor.ReactorStructureIssue;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FissionReactorControllerScreen extends AbstractContainerScreen<FissionReactorControllerContainerMenu> {
        private static final int GUI_WIDTH = 197;
    private static final int GUI_HEIGHT = 198;
        private static final int TEXT = 0xB9E6BC;
        private static final int TEXT_BRIGHT = 0xD9F5CD;
        private static final int TEXT_DIM = 0x71977A;
        private static final int HEAT = 0xD07158;
        private static final int NEUTRON = 0xD0C05C;
        private static final int ENERGY = 0x62B99A;
        private static final int WARNING = 0xD5BC63;
        private static final int ERROR = 0xDD6A59;
        private static final int TRACK = 0x0B100D;

        private static final ResourceLocation GUI = new ResourceLocation(
                        DustAndAsh.MOD_ID,
                        "textures/gui/fission_reactor_controller_gui.png"
        );

    public FissionReactorControllerScreen(FissionReactorControllerContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
                this.imageWidth = GUI_WIDTH;
                this.imageHeight = GUI_HEIGHT;
                this.titleLabelX = 7;
                this.titleLabelY = 4;
                this.inventoryLabelX = 17;
                this.inventoryLabelY = 111;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
                renderDiagnosticTooltip(guiGraphics, pMouseX, pMouseY);
        renderTelemetryTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTicks, int pX, int pY) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);

        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(GUI, i, j, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        drawBar(guiGraphics, i + 9, j + 28, 116, this.menu.getHeat(),
                FissionReactorControllerEntity.MAX_HEAT, HEAT);
        drawBar(guiGraphics, i + 9, j + 45, 116, this.menu.getNeutron(),
                FissionReactorControllerEntity.MAX_NEUTRON, NEUTRON);
        drawBar(guiGraphics, i + 9, j + 62, 116, this.menu.getEnergy(),
                FissionReactorControllerEntity.MAX_ENERGY, ENERGY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {
        Component terminalTitle = Component.literal("> ").append(this.title);
        guiGraphics.drawString(this.font, terminalTitle, this.titleLabelX, this.titleLabelY, TEXT_BRIGHT, false);
        drawRightAligned(guiGraphics, getOperatingStateShortComponent(), 190, 4, getOperatingStateColor());

        drawMeterLabel(guiGraphics,
                Component.translatable("screen.dustandash.fission_reactor_controller_heat"),
                this.menu.getHeat(), FissionReactorControllerEntity.MAX_HEAT, 8, 18);
        drawMeterLabel(guiGraphics,
                Component.translatable("screen.dustandash.fission_reactor_controller_neutron"),
                this.menu.getNeutron(), FissionReactorControllerEntity.MAX_NEUTRON, 8, 35);
        drawMeterLabel(guiGraphics,
                Component.translatable("screen.dustandash.fission_reactor_controller_energy"),
                this.menu.getEnergy(), FissionReactorControllerEntity.MAX_ENERGY, 8, 52);

        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.efficiency",
                String.valueOf(this.menu.getEfficiency()), 132, 18);
        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.fuel",
                String.valueOf(this.menu.getFuelCellCount()), 132, 27);
        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.cooling",
                String.valueOf(this.menu.getCoolingCellCount()), 132, 36);
        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.radius",
                String.valueOf(this.menu.getRadius()), 132, 45);
        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.height",
                String.valueOf(this.menu.getHeight()), 132, 54);
        drawStat(guiGraphics, "screen.dustandash.fission_reactor_controller.stat.rate",
                formatCompact(this.menu.getEnergyGenerationRate()), 132, 63);

        Component status = Component.translatable("screen.dustandash.fission_reactor_controller.status_prefix")
                .append(getOperatingStateComponent());
        drawFittedString(guiGraphics, status, 8, 79, 181, getOperatingStateColor());
        drawFittedString(guiGraphics, getDiagnosticComponent(), 29, 92, 160, TEXT);
        drawFittedString(guiGraphics, getNeutronSlotComponent(), 29, 102, 160, getNeutronSlotColor());
    }

    private void drawMeterLabel(GuiGraphics guiGraphics, Component label, int value, int maximum, int x, int y) {
        guiGraphics.drawString(this.font, label, x, y, TEXT_DIM, false);
        Component amount = Component.literal(formatCompact(value) + " / " + formatCompact(maximum));
        drawRightAligned(guiGraphics, amount, 125, y, TEXT_BRIGHT);
    }

    private void drawStat(GuiGraphics guiGraphics, String translationKey, String value, int x, int y) {
        drawFittedString(guiGraphics, Component.translatable(translationKey), x, y, 37, TEXT_DIM);
        drawRightAligned(guiGraphics, Component.literal(value), 189, y, TEXT_BRIGHT);
    }

    private void drawRightAligned(GuiGraphics guiGraphics, Component text, int right, int y, int color) {
        guiGraphics.drawString(this.font, text, right - this.font.width(text), y, color, false);
    }

    private void drawFittedString(GuiGraphics guiGraphics, Component text, int x, int y, int maximumWidth) {
        drawFittedString(guiGraphics, text, x, y, maximumWidth, TEXT);
    }

    private void drawFittedString(GuiGraphics guiGraphics, Component text, int x, int y,
                                  int maximumWidth, int color) {
        int width = this.font.width(text);
        if (width <= maximumWidth) {
            guiGraphics.drawString(this.font, text, x, y, color, false);
            return;
        }

        float scale = maximumWidth / (float) width;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1f);
        guiGraphics.drawString(this.font, text, 0, 0, color, false);
        guiGraphics.pose().popPose();
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, int width,
                         int value, int maximum, int color) {
        guiGraphics.fill(x, y, x + width, y + 4, TRACK);
        int filled = Mth.clamp((int) Math.round(width * (value / (double) maximum)), 0, width);
        if (filled > 0) {
            guiGraphics.fill(x, y, x + filled, y + 4, color);
        }
    }

    private Component getOperatingStateComponent() {
        String suffix = switch (this.menu.getOperatingState()) {
            case SCANNING -> "scanning";
            case MALFORMED -> "malformed";
            case MISSING_INTERFACE -> "missing_interface";
            case MISSING_FUEL_CELL -> "missing_fuel_cell";
            case WAITING_FOR_FUEL -> "waiting_for_fuel";
            case COOLING_DOWN -> "cooling_down";
            case RUNNING -> "running";
        };
        return Component.translatable("screen.dustandash.fission_reactor_controller.status." + suffix);
    }

    private Component getOperatingStateShortComponent() {
        String suffix = switch (this.menu.getOperatingState()) {
            case SCANNING -> "scanning";
            case MALFORMED -> "malformed";
            case MISSING_INTERFACE -> "missing_interface";
            case MISSING_FUEL_CELL -> "missing_fuel_cell";
            case WAITING_FOR_FUEL -> "waiting_for_fuel";
            case COOLING_DOWN -> "cooling_down";
            case RUNNING -> "running";
        };
        return Component.translatable("screen.dustandash.fission_reactor_controller.status_short." + suffix);
    }

    private int getOperatingStateColor() {
        return switch (this.menu.getOperatingState()) {
            case MALFORMED -> ERROR;
            case MISSING_INTERFACE, MISSING_FUEL_CELL, WAITING_FOR_FUEL, COOLING_DOWN -> WARNING;
            case RUNNING -> ENERGY;
            case SCANNING -> TEXT_DIM;
        };
    }

    private Component getDiagnosticComponent() {
        ReactorStructureIssue issue = this.menu.getStructureIssue();
        if (issue == ReactorStructureIssue.INVALID_CASING || issue == ReactorStructureIssue.INVALID_WALL) {
            BlockPos pos = this.menu.getProblemPos();
            return Component.translatable(
                    "screen.dustandash.fission_reactor_controller.issue."
                            + (issue == ReactorStructureIssue.INVALID_CASING ? "invalid_casing" : "invalid_wall"),
                    pos.getX(), pos.getY(), pos.getZ()
            );
        }
        if (issue == ReactorStructureIssue.RADIUS_ANCHOR_MISSING) {
            return Component.translatable(
                    "screen.dustandash.fission_reactor_controller.issue.radius_anchor_missing");
        }
        if (issue == ReactorStructureIssue.HEIGHT_ANCHOR_MISSING) {
            return Component.translatable(
                    "screen.dustandash.fission_reactor_controller.issue.height_anchor_missing");
        }

        String suffix = switch (this.menu.getOperatingState()) {
            case MISSING_INTERFACE -> null;
            case MISSING_FUEL_CELL -> "missing_fuel_cell";
            case WAITING_FOR_FUEL -> "waiting_for_fuel";
            case COOLING_DOWN -> "cooling_down";
            case RUNNING -> "running";
            case SCANNING, MALFORMED -> "scanning";
        };
        if (suffix == null) {
            BlockPos pos = this.menu.getProblemPos();
            return Component.translatable(
                    "screen.dustandash.fission_reactor_controller.detail.missing_interface",
                    pos.getX(), pos.getY(), pos.getZ()
            );
        }
        return Component.translatable("screen.dustandash.fission_reactor_controller.detail." + suffix);
    }

    private Component getNeutronSlotComponent() {
        NeutronSlotState state = this.menu.getNeutronSlotState();
        String key = "screen.dustandash.fission_reactor_controller.neutron_slot.";
        if (state == NeutronSlotState.ABSORBING || state == NeutronSlotState.FULL) {
            int stored = NeutronContainerUpdater.getNeutron(this.menu.getNeutronContainer());
            return Component.translatable(key + (state == NeutronSlotState.FULL ? "full" : "absorbing"),
                    stored, NeutronContainerUpdater.MAX_NEUTRON);
        }
        return Component.translatable(key + switch (state) {
            case EMPTY -> "empty";
            case INVALID_ITEM -> "invalid_item";
            case WAITING_FOR_NEUTRONS -> "waiting";
            case ABSORBING -> "absorbing";
            case FULL -> "full";
        });
    }

    private int getNeutronSlotColor() {
        return switch (this.menu.getNeutronSlotState()) {
            case INVALID_ITEM -> ERROR;
            case WAITING_FOR_NEUTRONS -> WARNING;
            case ABSORBING, FULL -> NEUTRON;
            case EMPTY -> TEXT_DIM;
        };
    }

    private void renderDiagnosticTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!isHovering(5, 77, 187, 35, mouseX, mouseY)
                || isHovering(7, 92, 18, 18, mouseX, mouseY)) {
            return;
        }

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(getOperatingStateComponent());
        tooltip.add(getDiagnosticComponent());
        if (this.menu.getStructureIssue() == ReactorStructureIssue.INVALID_CASING
                || this.menu.getStructureIssue() == ReactorStructureIssue.INVALID_WALL) {
            BlockPos controller = this.menu.getTileEntity().getBlockPos();
            BlockPos problem = this.menu.getProblemPos();
            if (this.minecraft != null && this.minecraft.level != null) {
                tooltip.add(Component.translatable(
                        "screen.dustandash.fission_reactor_controller.issue.current_block",
                        this.minecraft.level.getBlockState(problem).getBlock().getName()
                ));
            }
            tooltip.add(Component.translatable(
                    "screen.dustandash.fission_reactor_controller.issue.relative",
                    problem.getX() - controller.getX(),
                    problem.getY() - controller.getY(),
                    problem.getZ() - controller.getZ()
            ));
        }
        tooltip.add(getNeutronSlotComponent());
        guiGraphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
    }

    private void renderTelemetryTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component tooltip = null;
        if (isHovering(8, 18, 118, 15, mouseX, mouseY)) {
            tooltip = Component.translatable("screen.dustandash.fission_reactor_controller.telemetry.heat",
                    this.menu.getHeat(), FissionReactorControllerEntity.MAX_HEAT);
        } else if (isHovering(8, 35, 118, 15, mouseX, mouseY)) {
            tooltip = Component.translatable("screen.dustandash.fission_reactor_controller.telemetry.neutron",
                    this.menu.getNeutron(), FissionReactorControllerEntity.MAX_NEUTRON);
        } else if (isHovering(8, 52, 118, 15, mouseX, mouseY)) {
            tooltip = Component.translatable("screen.dustandash.fission_reactor_controller.telemetry.energy",
                    this.menu.getEnergy(), FissionReactorControllerEntity.MAX_ENERGY);
        } else if (isHovering(130, 62, 62, 10, mouseX, mouseY)) {
            tooltip = Component.translatable("screen.dustandash.fission_reactor_controller.telemetry.rate",
                    this.menu.getEnergyGenerationRate());
        }
        if (tooltip != null) {
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }

    private static String formatCompact(int value) {
        long magnitude = Math.abs((long) value);
        if (magnitude >= 1_000_000_000L) {
            return String.format(Locale.ROOT, "%.2fG", value / 1_000_000_000d);
        }
        if (magnitude >= 1_000_000L) {
            return String.format(Locale.ROOT, "%.2fM", value / 1_000_000d);
        }
        if (magnitude >= 1_000L) {
            return String.format(Locale.ROOT, "%.1fk", value / 1_000d);
        }
        return String.valueOf(value);
    }

}
