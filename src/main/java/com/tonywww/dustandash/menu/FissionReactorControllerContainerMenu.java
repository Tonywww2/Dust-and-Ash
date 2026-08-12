package com.tonywww.dustandash.menu;

import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronSlotState;
import com.tonywww.dustandash.block.entity.FissionReactor.ReactorOperatingState;
import com.tonywww.dustandash.block.entity.FissionReactor.ReactorStructureIssue;
import net.minecraft.core.BlockPos;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FissionReactorControllerContainerMenu extends AbstractMachineMenu<FissionReactorControllerEntity> {
    private final ContainerData data;

    public FissionReactorControllerContainerMenu(int id, Inventory playerInventory,
                                                  FissionReactorControllerEntity tileEntity, ContainerData data) {
        super(DAAContainerMenus.FISSION_REACTOR_CONTROLLER_CONTAINER.get(), id, playerInventory, tileEntity,
            DAABlocks.FISSION_REACTOR_CONTROLLER.get(), 1, 17, 117);
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
        return combineWords(
            this.data.get(FissionReactorControllerEntity.DATA_HEAT_LOW),
            this.data.get(FissionReactorControllerEntity.DATA_HEAT_HIGH)
        );
    }

    public int getEnergy() {
        return combineWords(
            this.data.get(FissionReactorControllerEntity.DATA_ENERGY_LOW),
            this.data.get(FissionReactorControllerEntity.DATA_ENERGY_HIGH)
        );
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
        return combineWords(
            this.data.get(FissionReactorControllerEntity.DATA_EFFICIENCY_LOW),
            this.data.get(FissionReactorControllerEntity.DATA_EFFICIENCY_HIGH)
        );
    }

    public ReactorOperatingState getOperatingState() {
        return ReactorOperatingState.byId(this.data.get(FissionReactorControllerEntity.DATA_OPERATING_STATE));
    }

    public ReactorStructureIssue getStructureIssue() {
        return ReactorStructureIssue.byId(this.data.get(FissionReactorControllerEntity.DATA_STRUCTURE_ISSUE));
    }

    public BlockPos getProblemPos() {
        return new BlockPos(
            combineWords(
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_X_LOW),
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_X_HIGH)
            ),
            combineWords(
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_Y_LOW),
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_Y_HIGH)
            ),
            combineWords(
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_Z_LOW),
                this.data.get(FissionReactorControllerEntity.DATA_PROBLEM_Z_HIGH)
            )
        );
    }

    public NeutronSlotState getNeutronSlotState() {
        return NeutronSlotState.byId(this.data.get(FissionReactorControllerEntity.DATA_NEUTRON_SLOT_STATE));
    }

    public int getEnergyGenerationRate() {
        return combineWords(
            this.data.get(FissionReactorControllerEntity.DATA_ENERGY_RATE_LOW),
            this.data.get(FissionReactorControllerEntity.DATA_ENERGY_RATE_HIGH)
        );
    }

    public ItemStack getNeutronContainer() {
        return this.slots.get(PLAYER_SLOT_COUNT).getItem();
    }

    private static int combineWords(int low, int high) {
        return (low & 0xFFFF) | (high << 16);
    }
}