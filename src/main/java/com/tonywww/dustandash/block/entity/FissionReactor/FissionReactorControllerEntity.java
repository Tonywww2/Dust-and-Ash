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

    public static final int DATA_HEAT_LOW = 0;
    public static final int DATA_HEAT_HIGH = 1;
    public static final int DATA_EFFICIENCY_LOW = 2;
    public static final int DATA_EFFICIENCY_HIGH = 3;
    public static final int DATA_RADIUS = 4;
    public static final int DATA_HEIGHT = 5;
    public static final int DATA_NEUTRON = 6;
    public static final int DATA_FUEL_CELL_COUNT = 7;
    public static final int DATA_COOLING_CELL_COUNT = 8;
    public static final int DATA_ENERGY_LOW = 9;
    public static final int DATA_ENERGY_HIGH = 10;
    public static final int DATA_OPERATING_STATE = 11;
    public static final int DATA_STRUCTURE_ISSUE = 12;
    public static final int DATA_PROBLEM_X_LOW = 13;
    public static final int DATA_PROBLEM_X_HIGH = 14;
    public static final int DATA_PROBLEM_Y_LOW = 15;
    public static final int DATA_PROBLEM_Y_HIGH = 16;
    public static final int DATA_PROBLEM_Z_LOW = 17;
    public static final int DATA_PROBLEM_Z_HIGH = 18;
    public static final int DATA_NEUTRON_SLOT_STATE = 19;
    public static final int DATA_ENERGY_RATE_LOW = 20;
    public static final int DATA_ENERGY_RATE_HIGH = 21;
    public static final int DATA_COUNT = 22;

    private double heat = 0;
    private int energy = 0;
    private int radius = 0;
    private int height = 0;
    private int neutron = 0;
    private int fuelCellCount = 0;
    private int coolingCellCount = 0;
    private int efficiency = 0;
    private int energyGenerationPerWorkTick = 0;
    private ReactorOperatingState operatingState = ReactorOperatingState.SCANNING;
    private ReactorStructureIssue structureIssue = ReactorStructureIssue.NONE;
    private BlockPos problemPos = BlockPos.ZERO;
    private NeutronSlotState neutronSlotState = NeutronSlotState.EMPTY;

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
                    case DATA_HEAT_LOW -> {
                        return lowWord((int) heat);
                    }
                    case DATA_HEAT_HIGH -> {
                        return highWord((int) heat);
                    }
                    case DATA_EFFICIENCY_LOW -> {
                        return lowWord(efficiency);
                    }
                    case DATA_EFFICIENCY_HIGH -> {
                        return highWord(efficiency);
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
                    case DATA_ENERGY_LOW -> {
                        return lowWord(energy);
                    }
                    case DATA_ENERGY_HIGH -> {
                        return highWord(energy);
                    }
                    case DATA_OPERATING_STATE -> {
                        return operatingState.getId();
                    }
                    case DATA_STRUCTURE_ISSUE -> {
                        return structureIssue.getId();
                    }
                    case DATA_PROBLEM_X_LOW -> {
                        return lowWord(problemPos.getX());
                    }
                    case DATA_PROBLEM_X_HIGH -> {
                        return highWord(problemPos.getX());
                    }
                    case DATA_PROBLEM_Y_LOW -> {
                        return lowWord(problemPos.getY());
                    }
                    case DATA_PROBLEM_Y_HIGH -> {
                        return highWord(problemPos.getY());
                    }
                    case DATA_PROBLEM_Z_LOW -> {
                        return lowWord(problemPos.getZ());
                    }
                    case DATA_PROBLEM_Z_HIGH -> {
                        return highWord(problemPos.getZ());
                    }
                    case DATA_NEUTRON_SLOT_STATE -> {
                        return neutronSlotState.getId();
                    }
                    case DATA_ENERGY_RATE_LOW -> {
                        return lowWord(energyGenerationPerWorkTick);
                    }
                    case DATA_ENERGY_RATE_HIGH -> {
                        return highWord(energyGenerationPerWorkTick);
                    }
                }
                return Integer.MIN_VALUE;
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case DATA_HEAT_LOW:
                        heat = withLowWord((int) heat, val);
                        break;

                    case DATA_HEAT_HIGH:
                        heat = withHighWord((int) heat, val);
                        break;

                    case DATA_EFFICIENCY_LOW:
                        efficiency = withLowWord(efficiency, val);
                        break;

                    case DATA_EFFICIENCY_HIGH:
                        efficiency = withHighWord(efficiency, val);
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

                    case DATA_ENERGY_LOW:
                        energy = withLowWord(energy, val);
                        break;

                    case DATA_ENERGY_HIGH:
                        energy = withHighWord(energy, val);
                        break;

                    case DATA_OPERATING_STATE:
                        operatingState = ReactorOperatingState.byId(val);
                        break;

                    case DATA_STRUCTURE_ISSUE:
                        structureIssue = ReactorStructureIssue.byId(val);
                        break;

                    case DATA_PROBLEM_X_LOW:
                        problemPos = new BlockPos(withLowWord(problemPos.getX(), val),
                                problemPos.getY(), problemPos.getZ());
                        break;

                    case DATA_PROBLEM_X_HIGH:
                        problemPos = new BlockPos(withHighWord(problemPos.getX(), val),
                                problemPos.getY(), problemPos.getZ());
                        break;

                    case DATA_PROBLEM_Y_LOW:
                        problemPos = new BlockPos(problemPos.getX(),
                                withLowWord(problemPos.getY(), val), problemPos.getZ());
                        break;

                    case DATA_PROBLEM_Y_HIGH:
                        problemPos = new BlockPos(problemPos.getX(),
                                withHighWord(problemPos.getY(), val), problemPos.getZ());
                        break;

                    case DATA_PROBLEM_Z_LOW:
                        problemPos = new BlockPos(problemPos.getX(), problemPos.getY(),
                                withLowWord(problemPos.getZ(), val));
                        break;

                    case DATA_PROBLEM_Z_HIGH:
                        problemPos = new BlockPos(problemPos.getX(), problemPos.getY(),
                                withHighWord(problemPos.getZ(), val));
                        break;

                    case DATA_NEUTRON_SLOT_STATE:
                        neutronSlotState = NeutronSlotState.byId(val);
                        break;

                    case DATA_ENERGY_RATE_LOW:
                        energyGenerationPerWorkTick = withLowWord(energyGenerationPerWorkTick, val);
                        break;

                    case DATA_ENERGY_RATE_HIGH:
                        energyGenerationPerWorkTick = withHighWord(energyGenerationPerWorkTick, val);
                        break;

                }

            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };

    }

    private static int lowWord(int value) {
        return value & 0xFFFF;
    }

    private static int highWord(int value) {
        return value >>> 16;
    }

    private static int withLowWord(int current, int low) {
        return (current & 0xFFFF0000) | (low & 0xFFFF);
    }

    private static int withHighWord(int current, int high) {
        return (current & 0xFFFF) | (high << 16);
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
                be.energyGenerationPerWorkTick = 0;
                be.updateReactorState(level, pos);
                be.resetWorkCycle();

            }
            be.neutronBombardment();
            be.distributeEnergy();

        }


    }

    private void updateReactorState(Level level, BlockPos pos) {
        ReactorStructureDiagnostic diagnostic = ReactorStructureScanner.diagnose(
                level,
                pos,
                MAX_RADIUS,
                MAX_HEIGHT
        );
        this.structureIssue = diagnostic.issue();
        this.problemPos = diagnostic.problemPos() == null ? BlockPos.ZERO : diagnostic.problemPos();

        if (!diagnostic.isFormed()) {
            this.radius = 0;
            this.height = 0;
            this.fuelCellCount = 0;
            this.coolingCellCount = 0;
            this.efficiency = 0;
            this.operatingState = ReactorOperatingState.MALFORMED;
            inventoryChanged();
            return;
        }

        ReactorStructureSnapshot structure = diagnostic.structure();
        this.radius = structure.radius();
        this.height = structure.height();
        ReactorCoreSnapshot core = ReactorStructureScanner.scanCore(level, pos, structure);
        this.fuelCellCount = core.fuelCellCount();
        this.coolingCellCount = core.coolingCellCount();

        if (structure.interfacePos() == null
                || !(level.getBlockEntity(structure.interfacePos()) instanceof FissionReactorInterfaceEntity intFace)) {
            this.efficiency = 0;
            this.problemPos = ReactorStructureScanner.getSuggestedInterfacePos(
                pos,
                structure.radius(),
                structure.height()
            );
            this.operatingState = ReactorOperatingState.MISSING_INTERFACE;
            inventoryChanged();
            return;
        }

        ItemStack fuel = intFace.itemStackHandler.getStackInSlot(FissionReactorInterfaceEntity.FUEL_INPUT_SLOT);
        if (this.fuelCellCount == 0) {
            this.operatingState = ReactorOperatingState.MISSING_FUEL_CELL;
        } else if (fuel.isEmpty() || !(fuel.getItem() instanceof FissionReactorFuelUnit)) {
            this.operatingState = this.heat > 0
                    ? ReactorOperatingState.COOLING_DOWN
                    : ReactorOperatingState.WAITING_FOR_FUEL;
        } else {
            this.operatingState = ReactorOperatingState.RUNNING;
        }

        runReactorCycle(level, core, intFace);
    }

    private void runReactorCycle(Level level, ReactorCoreSnapshot core,
                                 FissionReactorInterfaceEntity intFace) {
        ItemStack fuel = intFace.itemStackHandler.getStackInSlot(FissionReactorInterfaceEntity.FUEL_INPUT_SLOT);
        ItemStack cooling = intFace.itemStackHandler.getStackInSlot(FissionReactorInterfaceEntity.COOLING_INPUT_SLOT);

        if (!fuel.isEmpty() && fuel.getItem() instanceof FissionReactorFuelUnit fuelUnit) {
            FissionReactorCoolingUnit coolingUnit = cooling.getItem() instanceof FissionReactorCoolingUnit unit
                    ? unit
                    : null;
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
        if (stack.isEmpty()) {
            this.neutronSlotState = NeutronSlotState.EMPTY;
            return;
        }
        if (!stack.is(ModTags.Items.NEUTRON_CONTAINER)) {
            this.neutronSlotState = NeutronSlotState.INVALID_ITEM;
            return;
        }
        if (NeutronContainerUpdater.getNeutron(stack) >= NeutronContainerUpdater.MAX_NEUTRON) {
            this.neutronSlotState = NeutronSlotState.FULL;
            return;
        }
        if (this.neutron <= 0) {
            this.neutronSlotState = NeutronSlotState.WAITING_FOR_NEUTRONS;
            return;
        }

        NeutronContainerUpdater.UpdateResult result = NeutronContainerUpdater.absorbOne(stack);
        if (result.absorbed()) {
            this.neutron--;
        }
        this.neutronSlotState = NeutronContainerUpdater.getNeutron(stack) >= NeutronContainerUpdater.MAX_NEUTRON
                ? NeutronSlotState.FULL
                : NeutronSlotState.ABSORBING;
        if (result.changed()) {
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
