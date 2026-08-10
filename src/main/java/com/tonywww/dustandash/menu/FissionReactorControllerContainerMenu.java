package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FissionReactorControllerContainerMenu extends AbstractMachineMenu<FissionReactorControllerEntity> {
    private final ContainerData data;

    public FissionReactorControllerContainerMenu(int id, Inventory playerInventory,
                                                  FissionReactorControllerEntity tileEntity, ContainerData data) {
        super(DAAContainerMenus.FISSION_REACTOR_CONTROLLER_CONTAINER.get(), id, playerInventory, tileEntity,
                DAABlocks.FISSION_REACTOR_CONTROLLER.get(), 1, 3, 117);
        this.data = data;

        tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler ->
                addSlot(new SlotItemHandler(handler, 0, 7, 92)));
        addDataSlots(data);
    }

    public FissionReactorControllerContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory,
                readBlockEntity(playerInventory, data, FissionReactorControllerEntity.class),
                new SimpleContainerData(FissionReactorControllerEntity.DATA_COUNT));
    }

    public ContainerData getData() {
        return this.data;
    }

    public FissionReactorControllerEntity getTileEntity() {
        return this.tileEntity;
    }

    public int getHeat() {
        return this.data.get(FissionReactorControllerEntity.DATA_HEAT);
    }

    public int getEnergy() {
        return this.data.get(FissionReactorControllerEntity.DATA_ENERGY);
    }

    public int getRadius() {
        return this.data.get(FissionReactorControllerEntity.DATA_RADIUS);
    }

    public int getHeight() {
        return this.data.get(FissionReactorControllerEntity.DATA_HEIGHT);
    }

    public int getNeutron() {
        return this.data.get(FissionReactorControllerEntity.DATA_NEUTRON);
    }

    public int getFuelCellCount() {
        return this.data.get(FissionReactorControllerEntity.DATA_FUEL_CELL_COUNT);
    }

    public int getCoolingCellCount() {
        return this.data.get(FissionReactorControllerEntity.DATA_COOLING_CELL_COUNT);
    }

    public int getEfficiency() {
        return this.data.get(FissionReactorControllerEntity.DATA_EFFICIENCY);
    }
}