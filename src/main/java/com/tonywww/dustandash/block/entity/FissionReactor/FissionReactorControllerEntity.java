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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class FissionReactorControllerEntity extends BasicMachineEntity implements MenuProvider {

    public ItemStackHandler invItemStackHandler;
    public EnergyStorage energyStorage;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<EnergyStorage> energyStorageHandler;
    protected final ContainerData dataAccess;

    public static final int MAX_HEAT = 50000;
    public static final int MAX_FUEL = 4000;
    public static final int MAX_ENERGY = 500000000;
    public static final int MAX_RADIUS = 3;
    public static final int MAX_HEIGHT = 7;
    public static final int MAX_NEUTRON = 128;

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
        this.energyStorage = createEnergyHandler();
        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.energyStorageHandler = LazyOptional.of(() -> energyStorage);

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

    private EnergyStorage createEnergyHandler() {
        return new EnergyStorage(MAX_ENERGY) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {

                return 0;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int energy = this.getEnergyStored();
                int diff = Math.min(energy, maxExtract);
                if (!simulate) {
                    FissionReactorControllerEntity.this.energy += diff;
                    if (diff != 0) {
                        FissionReactorControllerEntity.this.inventoryChanged();
                    }
                }
                return diff;
            }

            @Override
            public int getEnergyStored() {
                return Math.max(0, Math.min(this.getMaxEnergyStored(), FissionReactorControllerEntity.this.energy));
            }

            @Override
            public int getMaxEnergyStored() {
                return FissionReactorControllerEntity.MAX_ENERGY;
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return false;
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

    public static void tick(Level level, BlockPos pos, BlockState state, FissionReactorControllerEntity be) {
        if (!level.isClientSide) {
            BasicMachineEntity.tick(be, 1);
            if (BasicMachineEntity.isWorkingTick(be)) {
                if (be.checkStructure()) {
                    double efficiency = 1000000.0;
                    be.energy = (int) Math.min(FissionReactorControllerEntity.MAX_ENERGY, be.energy + efficiency);

                }
                BasicMachineEntity.resetTicker(be);

            }

        }


    }

    private boolean checkStructure() {
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {

        if (!this.remove && cap == ForgeCapabilities.ENERGY) {
            return this.energyStorageHandler.cast();
        }

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
