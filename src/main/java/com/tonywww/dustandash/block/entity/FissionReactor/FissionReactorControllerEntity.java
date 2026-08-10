package com.tonywww.dustandash.block.entity.FissionReactor;

import com.google.common.collect.Queues;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.block.entity.BasicMachineEntity;
import com.tonywww.dustandash.block.entity.DroppableInventory;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import com.tonywww.dustandash.registry.DAABlockEntities;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.tag.ModTags;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.Queue;

public class FissionReactorControllerEntity extends BasicMachineEntity implements MenuProvider, DroppableInventory {
    public ItemStackHandler invItemStackHandler;
    public EnergyStorage energyStorage;
    private final ManagedCapability<ItemStackHandler> handler;
    private final ManagedCapability<EnergyStorage> energyStorageHandler;
    protected final ContainerData dataAccess;

    public static final int MAX_HEAT = 50000;
    public static final int MAX_ENERGY = 2000000000;
    public static final int MAX_RADIUS = 3;
    public static final int MAX_HEIGHT = 7;
    public static final int MAX_NEUTRON = 4096;
    public static final int MAX_TRANSFER = 400000000;
    public static final String NEUTRON_TAG = "neutron";
    public static final int MAX_NEUTRON_FOR_ITEM = 1280;

    public static final int DATA_HEAT = 0;
    public static final int DATA_EFFICIENCY = 1;
    public static final int DATA_RADIUS = 2;
    public static final int DATA_HEIGHT = 3;
    public static final int DATA_NEUTRON = 4;
    public static final int DATA_FUEL_CELL_COUNT = 5;
    public static final int DATA_COOLING_CELL_COUNT = 6;
    public static final int DATA_ENERGY = 7;
    public static final int DATA_COUNT = 8;

    private double heat = 0;
    private int energy = 0;
    private int radius = 0;
    private int height = 0;
    private int neutron = 0;
    private int fuelCellCount = 0;
    private int coolingCellCount = 0;
    private int efficiency = 0;
    private int energyGenerationPerWorkTick = 0;

    public FissionReactorControllerEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get(), pos, state);

        this.invItemStackHandler = createHandler();
        this.energyStorage = createEnergyHandler();
        this.handler = managedCapability(() -> invItemStackHandler);
        this.energyStorageHandler = managedCapability(() -> energyStorage);

        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case DATA_HEAT -> {
                        return (int) heat;
                    }
                    case DATA_EFFICIENCY -> {
                        return efficiency;
                    }
                    case DATA_RADIUS -> {
                        return radius;
                    }
                    case DATA_HEIGHT -> {
                        return height;
                    }
                    case DATA_NEUTRON -> {
                        return neutron;
                    }
                    case DATA_FUEL_CELL_COUNT -> {
                        return fuelCellCount;
                    }
                    case DATA_COOLING_CELL_COUNT -> {
                        return coolingCellCount;
                    }
                    case DATA_ENERGY -> {
                        return energy;
                    }
                }
                return Integer.MIN_VALUE;
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case DATA_HEAT:
                        heat = val;
                        break;

                    case DATA_EFFICIENCY:
                        efficiency = val;
                        break;

                    case DATA_RADIUS:
                        radius = val;
                        break;

                    case DATA_HEIGHT:
                        height = val;
                        break;

                    case DATA_NEUTRON:
                        neutron = val;
                        break;

                    case DATA_FUEL_CELL_COUNT:
                        fuelCellCount = val;
                        break;

                    case DATA_COOLING_CELL_COUNT:
                        coolingCellCount = val;
                        break;

                    case DATA_ENERGY:
                        energy = val;
                        break;

                }

            }

            @Override
            public int getCount() {
                return DATA_COUNT;
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
                    FissionReactorControllerEntity.this.energy -= diff;
                    if (diff != 0) {
                        FissionReactorControllerEntity.this.inventoryChanged();
                    }
                }
                return diff;
//                return 0;
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

    @Override
    public ItemStackHandler getInventory() {
        return this.invItemStackHandler;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FissionReactorControllerEntity be) {
        if (!level.isClientSide) {
            if (be.advanceWorkCycle(1)) {
                var structure = ReactorStructureScanner.scan(level, pos, MAX_RADIUS, MAX_HEIGHT);
                if (structure.isPresent()) {
                    ReactorStructureSnapshot snapshot = structure.get();
                    be.radius = snapshot.radius();
                    be.height = snapshot.height();

                    if (snapshot.interfacePos() != null
                            && level.getBlockEntity(snapshot.interfacePos()) instanceof FissionReactorInterfaceEntity intFace) {
                        be.runReactorCycle(level, pos, snapshot, intFace);
                    }
                } else {
                    be.radius = 0;
                    be.height = 0;
                }
                be.resetWorkCycle();

            }
            be.neutronBombardment();
            be.distributeEnergy();

        }


    }

    private void runReactorCycle(Level level, BlockPos pos, ReactorStructureSnapshot structure,
                                 FissionReactorInterfaceEntity intFace) {
        ItemStack fuel = intFace.itemStackHandler.getStackInSlot(FissionReactorInterfaceEntity.FUEL_INPUT_SLOT);
        ItemStack cooling = intFace.itemStackHandler.getStackInSlot(FissionReactorInterfaceEntity.COOLING_INPUT_SLOT);

        if (!fuel.isEmpty() && fuel.getItem() instanceof FissionReactorFuelUnit fuelUnit) {
            FissionReactorCoolingUnit coolingUnit = cooling.getItem() instanceof FissionReactorCoolingUnit unit
                    ? unit
                    : null;
            ReactorCoreSnapshot core = ReactorStructureScanner.scanCore(level, pos, structure);
            ReactorPhysicsEngine.FueledStep step = ReactorPhysicsEngine.calculateFueledStep(
                    core,
                    fuelUnit,
                    coolingUnit,
                    this.heat,
                    this.neutron,
                    this.tickPerOperation,
                    DustAndAshConfig.REACTOR.minimumEfficiency.get(),
                    DustAndAshConfig.REACTOR.maximumEfficiency.get(),
                    DustAndAshConfig.REACTOR.efficiencyMultiplier.get(),
                    DustAndAshConfig.REACTOR.idealHeatRate.get(),
                    MAX_HEAT
            );
            this.heat = step.heat();
            this.efficiency = step.efficiency();
            this.neutron = step.neutron();
            this.fuelCellCount = step.fuelCellCount();
            this.coolingCellCount = step.coolingCellCount();

            ReactorConsumableService.damageAndRecycle(
                    fuel,
                    this.fuelCellCount,
                    level.getRandom(),
                    intFace.itemStackHandler,
                    FissionReactorInterfaceEntity.FUEL_OUTPUT_SLOT,
                    DAAItems.EMPTY_FUEL_CONTAINER.get()
            );
            ReactorConsumableService.damageAndRecycle(
                    cooling,
                    this.coolingCellCount,
                    level.getRandom(),
                    intFace.itemStackHandler,
                    FissionReactorInterfaceEntity.COOLING_OUTPUT_SLOT,
                    DAAItems.EMPTY_FUEL_CONTAINER.get()
            );
        } else {
            this.heat = ReactorPhysicsEngine.coolDown(this.heat, MAX_HEAT);
        }

        if (this.neutron > MAX_NEUTRON / 2) {
            ReactorPhysicsEngine.EnergyStep energyStep = ReactorPhysicsEngine.convertExcessNeutrons(
                    this.neutron,
                    this.energy,
                    MAX_NEUTRON,
                    MAX_ENERGY,
                    DustAndAshConfig.REACTOR.neutronToEnergyRatio.get()
            );
            this.neutron = energyStep.neutron();
            this.energy = energyStep.energy();
            this.energyGenerationPerWorkTick = energyStep.generatedEnergy();
        }

        intFace.inventoryChanged();
        inventoryChanged();
    }

    private final Queue<Direction> directionQueue = Queues.newArrayDeque(Direction.Plane.HORIZONTAL);

    private void distributeEnergy() {
        if (this.energy <= 0 || this.level == null) {
            return;
        }
        int remainingEnergy = ReactorEnergyDistributor.distribute(
                this.level, this.worldPosition, this.directionQueue, this.energy, MAX_TRANSFER);
        if (remainingEnergy != this.energy) {
            this.energy = remainingEnergy;
            inventoryChanged();
        }
    }

    void neutronBombardment() {
        ItemStack stack = this.invItemStackHandler.getStackInSlot(0);
        if (this.neutron > 0 && stack.is(ModTags.Items.NEUTRON_CONTAINER)
                && NeutronContainerUpdater.absorbOne(stack, NEUTRON_TAG, MAX_NEUTRON_FOR_ITEM)) {
            this.neutron--;
            inventoryChanged();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {

        if (!this.remove && cap == ForgeCapabilities.ENERGY) {
            return this.energyStorageHandler.get().cast();
        }

        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.handler.get().cast();

        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        this.invItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));

        this.heat = compoundNBT.getDouble("heat");
        this.energy = compoundNBT.getInt("energy");
        this.neutron = compoundNBT.getInt("neutron");

        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", this.invItemStackHandler.serializeNBT());

        compound.putDouble("heat", this.heat);
        compound.putInt("energy", this.energy);
        compound.putInt("neutron", this.neutron);

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
