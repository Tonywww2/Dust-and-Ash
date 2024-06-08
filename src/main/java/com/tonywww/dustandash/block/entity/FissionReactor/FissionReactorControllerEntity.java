package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.block.entity.BasicMachineEntity;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import com.tonywww.dustandash.registeries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class FissionReactorControllerEntity extends BasicMachineEntity implements MenuProvider {

    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    protected final ContainerData dataAccess;

    static final int MAX_HEAT = 50000;
    static final int MAX_FUEL = 4000;
    static final int MAX_ENERGY = 500000000;
    static final int MAX_RADIUS = 3;
    static final int MAX_HEIGHT = 7;
    static final int MAX_NEUTRON = 128;

    private int heat = 0;
    private int fuel = 0;
    private int energy = 0;
    private int radius = 0;
    private int height = 0;
    private int neutron = 0;
    private int fuelCellCount = 0;
    private int coolingCellCount = 0;

    public FissionReactorControllerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get(), pos, state);

        this.invItemStackHandler = createHandler();
        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0 -> {
                        return heat;
                    }
                    case 1 -> {
                        return fuel;
                    }
                    case 2 -> {
                        return energy;
                    }
                    case 3 -> {
                        return radius;
                    }
                    case 4 -> {
                        return height;
                    }
                    case 5 -> {
                        return neutron;
                    }
                    case 6 -> {
                        return fuelCellCount;
                    }
                    case 7 -> {
                        return coolingCellCount;
                    }
                }
                return Integer.MIN_VALUE;
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case 0:
                        heat = val;

                    case 1:
                        fuel = val;

                    case 2:
                        energy = val;

                    case 3:
                        radius = val;

                    case 4:
                        height = val;

                    case 5:
                        neutron = val;

                    case 6:
                        fuelCellCount = val;

                    case 7:
                        coolingCellCount = val;

                }

            }

            @Override
            public int getCount() {
                return 6;
            }
        };

    }

    // 0 neutron
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

        };
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < invItemStackHandler.getSlots(); ++i) {
            drops.add(invItemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.handler.cast();

        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        invItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));

        heat = compoundNBT.getInt("heat");
        energy = compoundNBT.getInt("energy");
        fuel = compoundNBT.getInt("fuel");
        neutron = compoundNBT.getInt("neutron");

        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", invItemStackHandler.serializeNBT());
        compound.putInt("heat", heat);
        compound.putInt("energy", energy);
        compound.putInt("fuel", fuel);
        compound.putInt("neutron", neutron);

        super.saveAdditional(compound);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.fission_reactor_controller");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new FissionReactorControllerContainerMenu(id, playerInventory, this, dataAccess);
    }
}
