package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.block.entity.BasicMachineEntity;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import com.tonywww.dustandash.registeries.ModBlockEntities;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    public static final int MAX_ENERGY = 500000000;
    public static final int MAX_RADIUS = 3;
    public static final int MAX_HEIGHT = 7;
    public static final int MAX_NEUTRON = 2048;

    public static double minEfficiency = 1;
    public static double maxEfficiency = 12;
    public static double efficiencyMultiplayer = 1;
    public static double neutronToEnergy = 500;
    public static double idealHeatRate = 0.575;

    private double heat = 0;
    private int energy = 0;
    private int radius = 0;
    private int height = 0;
    private int neutron = 0;
    private int fuelCellCount = 0;
    private int coolingCellCount = 0;
    private int efficiency = 0;
    private int energyGenerationRate = 0;

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
                        return (int) heat;
                    }
                    case 1 -> {
                        return energyGenerationRate;
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
                    case 8 -> {
                        return efficiency;
                    }
                }
                return Integer.MIN_VALUE;
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case 0:
                        heat = val;
                        break;

                    case 1:
                        energyGenerationRate = val;
                        break;

                    case 2:
                        energy = val;
                        break;

                    case 3:
                        radius = val;
                        break;

                    case 4:
                        height = val;
                        break;

                    case 5:
                        neutron = val;
                        break;

                    case 6:
                        fuelCellCount = val;
                        break;

                    case 7:
                        coolingCellCount = val;
                        break;

                    case 8:
                        efficiency = val;
                        break;

                }

            }

            @Override
            public int getCount() {
                return 9;
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
                // check structure
                if (be.checkStructure(level, pos)) {
                    // find the interface
                    FissionReactorInterfaceEntity intFace = be.findInterface(level, pos);
                    if (intFace != null) {
                        ItemStack fuel = intFace.itemStackHandler.getStackInSlot(0);
                        ItemStack cool = intFace.itemStackHandler.getStackInSlot(1);
                        if (!fuel.isEmpty() && fuel.getItem() instanceof FissionReactorFuelUnit fuelUnit) {
                            // fuel available
                            FissionReactorCoolingUnit coolUnit = null;
                            if (!cool.isEmpty() && cool.getItem() instanceof FissionReactorCoolingUnit) {
                                coolUnit = (FissionReactorCoolingUnit) cool.getItem();
                            }
                            // calculate heat and change heat
                            double deltaHeat = be.calcHeat(level, pos, fuelUnit, coolUnit) - Math.sqrt(be.heat) - 1;
                            be.heat = Math.max(0, be.heat + deltaHeat);
                            be.efficiency = (int) (Math.max(minEfficiency,
                                    maxEfficiency - Math.pow(Math.abs(fuelUnit.getIdealHeat() - be.heat), idealHeatRate))
                                    * efficiencyMultiplayer * be.fuelCellCount);
                            be.neutron += (int) (be.efficiency * fuelUnit.getBaseNeutronRate());

                            if (fuel.hurt(be.fuelCellCount, level.getRandom(), null)) {
                                fuel.shrink(1);
                                intFace.itemStackHandler.insertItem(2, new ItemStack(Items.CLAY_BALL.asItem()), false).isEmpty();

                            }
                            if (!cool.isEmpty() && cool.hurt(be.coolingCellCount, level.getRandom(), null)) {
                                cool.shrink(1);
                                intFace.itemStackHandler.insertItem(3, new ItemStack(Items.DIRT.asItem()), false).isEmpty();

                            }

                        } else {
                            be.heat = Math.max(0, be.heat - Math.sqrt(be.heat) - 1);

                        }


                        if (be.neutron > MAX_NEUTRON / 2) {
                            be.energyGenerationRate = (int) ((be.neutron - (MAX_NEUTRON / 2d)) * neutronToEnergy);
                            be.energy = Math.min(FissionReactorControllerEntity.MAX_ENERGY, be.energy + be.energyGenerationRate);
                            be.neutron = MAX_NEUTRON / 2;

                        }

                        be.inventoryChanged();
                        intFace.inventoryChanged();

                    }

                } else {
                    be.radius = 0;
                    be.height = 0;
                }
                BasicMachineEntity.resetTicker(be);

            }

        }


    }

    private double calcHeat(Level level, BlockPos pos, FissionReactorFuelUnit fuel, FissionReactorCoolingUnit cool) {
        double totalHeat = 0;
        this.fuelCellCount = 0;
        this.coolingCellCount = 0;
        for (int i = 2; i < this.height; i++) {
            for (int j = 0; j < this.radius * 2 - 1; j++) {
                for (int k = 0; k < this.radius * 2 - 1; k++) {
                    BlockPos p = new BlockPos(pos.getX() - this.radius + 1 + j, pos.getY() - i, pos.getZ() - this.radius + 1 + k);
                    if (checkBlockPos(level, p, ModBlocks.FISSION_REACTOR_FUEL_CELL.get())) {
                        this.fuelCellCount++;
                        int surCoolCells = 0;
                        int surFuelCells = 0;

                        if (level.getBlockState(p.north()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.north()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        if (level.getBlockState(p.east()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.east()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        if (level.getBlockState(p.south()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.south()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        if (level.getBlockState(p.west()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.west()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        if (level.getBlockState(p.above()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.above()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        if (level.getBlockState(p.below()).getBlock() == ModBlocks.FISSION_REACTOR_COOLING_CELL.get())
                            surCoolCells++;
                        if (level.getBlockState(p.below()).getBlock() == ModBlocks.FISSION_REACTOR_FUEL_CELL.get())
                            surFuelCells++;

                        double coolRate = 1;
                        if (cool != null) coolRate = cool.getBaseCoolingRate();

                        double thisHeat = fuel.getBaseHeatRate() * Math.sqrt((surFuelCells + 1) / ((surCoolCells + 1) * coolRate));

                        totalHeat += thisHeat;

                    } else if (checkBlockPos(level, p, ModBlocks.FISSION_REACTOR_COOLING_CELL.get())) {
                        this.coolingCellCount++;
                    }

                }

            }
        }

//        System.out.println(this.fuelCellCount);
//        System.out.println(this.coolingCellCount);

        return totalHeat;

    }

    private FissionReactorInterfaceEntity findInterface(Level level, BlockPos pos) {
        FissionReactorInterfaceEntity out = null;
        BlockPos p = pos.below((this.height / 2) + 1);
        if (level.getBlockEntity(p.north(this.radius + 1)) instanceof FissionReactorInterfaceEntity entity) {
            out = entity;
        } else if (level.getBlockEntity(p.east(this.radius + 1)) instanceof FissionReactorInterfaceEntity entity) {
            out = entity;
        } else if (level.getBlockEntity(p.south(this.radius + 1)) instanceof FissionReactorInterfaceEntity entity) {
            out = entity;
        } else if (level.getBlockEntity(p.west(this.radius + 1)) instanceof FissionReactorInterfaceEntity entity) {
            out = entity;
        }
        return out;
    }

    private boolean checkStructure(Level level, BlockPos pos) {
        int r = 1;
        boolean flagRadius = false;
        BlockPos curr = pos.below();
        Block casing = ModBlocks.FISSION_REACTOR_CASING.get();

        for (; r <= MAX_RADIUS; r++) {
            curr = curr.north();
            if (level.getBlockState(curr).getBlock() == casing) {
                flagRadius = true;
                break;

            }

        }
        if (!flagRadius) return false;

        int h = 2;
        boolean flagHeight = false;
        curr = pos.north(r).below();
        for (; h <= MAX_HEIGHT; h++) {
            curr = curr.below();
            if (checkBlockPos(level, curr, casing)) {
                flagHeight = true;
                break;

            }
        }
        if (!flagHeight) return false;

        BlockPos s1 = pos.below().north(r).east(r);
        BlockPos s2 = pos.below(h).south(r).west(r);
        if (!checkBlockPos(level, s1, casing) || !checkBlockPos(level, s2, casing)) return false;

        BlockPos s11 = s1, s12 = s1, s13 = s1, s21 = s2, s22 = s2, s23 = s2;
        for (int i = 0; i < r * 2; i++) {
            s11 = s11.west();
            s12 = s12.south();
            s13 = s13.below();

            s21 = s21.north();
            s22 = s22.east();
            s23 = s23.above();
            if (!checkBlockPos(level, s11, casing)
                    || !checkBlockPos(level, s12, casing)
                    || !checkBlockPos(level, s13, casing)
                    || !checkBlockPos(level, s21, casing)
                    || !checkBlockPos(level, s22, casing)
                    || !checkBlockPos(level, s23, casing)
            ) return false;

        }

        this.radius = r;
        this.height = h;

        for (int i = 0; i < r * 2 - 1; i++) {
            for (int j = 0; j < r * 2 - 1; j++) {
                BlockPos p1 = new BlockPos(pos.getX() - r + 1 + i, pos.getY() - 1, pos.getZ() - r + 1 + j);
                BlockPos p2 = new BlockPos(pos.getX() - r + 1 + i, pos.getY() - h, pos.getZ() - r + 1 + j);

                BlockPos p3 = new BlockPos(pos.getX() - r, pos.getY() - h + 1 + j, pos.getZ() - r + 1 + i);
                BlockPos p4 = new BlockPos(pos.getX() + r, pos.getY() - h + 1 + j, pos.getZ() - r + 1 + i);

                BlockPos p5 = new BlockPos(pos.getX() - r + 1 + i, pos.getY() - h + 1 + j, pos.getZ() - r);
                BlockPos p6 = new BlockPos(pos.getX() - r + 1 + i, pos.getY() - h + 1 + j, pos.getZ() + r);

                if (!checkTagContains(level, p1, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || !checkTagContains(level, p2, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || !checkTagContains(level, p3, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || !checkTagContains(level, p4, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || !checkTagContains(level, p5, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || !checkTagContains(level, p6, ModTags.Blocks.FISSION_REACTOR_WALL)
                ) return false;

            }

        }

        return true;
    }

    boolean checkBlockPos(Level level, BlockPos pos, Block block) {
        return level.getBlockState(pos).getBlock() == block;
    }

    boolean checkTagContains(Level level, BlockPos pos, TagKey<Block> tag) {
        return level.getBlockState(pos).is(tag);
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

        heat = compoundNBT.getDouble("heat");
        energy = compoundNBT.getInt("energy");
        neutron = compoundNBT.getInt("neutron");

        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", invItemStackHandler.serializeNBT());
        compound.putDouble("heat", heat);
        compound.putInt("energy", energy);
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
