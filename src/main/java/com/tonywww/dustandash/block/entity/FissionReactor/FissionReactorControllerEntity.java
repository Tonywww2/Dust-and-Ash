package com.tonywww.dustandash.block.entity.FissionReactor;

import com.google.common.collect.Queues;
import com.tonywww.dustandash.block.entity.BasicMachineEntity;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import com.tonywww.dustandash.registeries.ModBlockEntities;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.registeries.ModItems;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

import static com.tonywww.dustandash.DustAndAshConfig.*;

public class FissionReactorControllerEntity extends BasicMachineEntity implements MenuProvider {
    public ItemStackHandler invItemStackHandler;
    public EnergyStorage energyStorage;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<EnergyStorage> energyStorageHandler;
    protected final ContainerData dataAccess;

    public static final int MAX_HEAT = 50000;
    public static final int MAX_ENERGY = 2000000000;
    public static final int MAX_RADIUS = 3;
    public static final int MAX_HEIGHT = 7;
    public static final int MAX_NEUTRON = 4096;
    public static final int MAX_TRANSFER = 400000000;
    public static final String NEUTRON_TAG = "neutron";
    public static final int MAX_NEUTRON_FOR_ITEM = 1280;

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
                        return efficiency;
                    }
                    case 2 -> {
                        return radius;
                    }
                    case 3 -> {
                        return height;
                    }
                    case 4 -> {
                        return neutron;
                    }
                    case 5 -> {
                        return fuelCellCount;
                    }
                    case 6 -> {
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
                        break;

                    case 1:
                        efficiency = val;
                        break;

                    case 2:
                        radius = val;
                        break;

                    case 3:
                        height = val;
                        break;

                    case 4:
                        neutron = val;
                        break;

                    case 5:
                        fuelCellCount = val;
                        break;

                    case 6:
                        coolingCellCount = val;
                        break;

                }

            }

            @Override
            public int getCount() {
                return 7;
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
                            be.efficiency = (int) (Math.max(fissionReactorMinEfficiency.get(),
                                    fissionReactorMaxEfficiency.get() -
                                            ((Math.pow(be.heat - fuelUnit.getIdealHeat(), 2)) / Math.pow(MAX_HEAT, fissionReactorIdealHeatRate.get()))
                            ) * fissionReactorEfficiencyMultiplayer.get() * be.fuelCellCount);
                            be.neutron += (int) (be.efficiency * fuelUnit.getBaseNeutronRate() * be.tickPerOperation);

                            if (fuel.hurt(be.fuelCellCount, level.getRandom(), null)) {
                                fuel.shrink(1);
                                if (intFace.itemStackHandler.getStackInSlot(2).is(ModItems.EMPTY_FUEL_CONTAINER.get())) {
                                    intFace.itemStackHandler.getStackInSlot(2).grow(1);

                                } else {
                                    intFace.itemStackHandler.setStackInSlot(2, new ItemStack(ModItems.EMPTY_FUEL_CONTAINER.get()));
                                }

                            }
                            if (!cool.isEmpty() && cool.hurt(be.coolingCellCount, level.getRandom(), null)) {
                                cool.shrink(1);
                                if (intFace.itemStackHandler.getStackInSlot(3).is(ModItems.EMPTY_FUEL_CONTAINER.get())) {
                                    intFace.itemStackHandler.getStackInSlot(3).grow(1);

                                } else {
                                    intFace.itemStackHandler.setStackInSlot(2, new ItemStack(ModItems.EMPTY_FUEL_CONTAINER.get()));
                                }
                            }

                        } else {
                            be.heat = Math.min(Math.max(0, be.heat - Math.sqrt(be.heat) - 1), MAX_HEAT);

                        }


                        if (be.neutron > MAX_NEUTRON / 2) {
                            int usedNeutron = be.neutron - (MAX_NEUTRON / 2);
                            be.neutron -= usedNeutron;

                            be.energyGenerationPerWorkTick = (int) (usedNeutron * fissionReactorNeutronToEnergyRatio.get());
                            be.energy = Math.min(FissionReactorControllerEntity.MAX_ENERGY, be.energy + be.energyGenerationPerWorkTick);

                        }

                        intFace.inventoryChanged();
                        be.inventoryChanged();

                    }

                } else {
                    be.radius = 0;
                    be.height = 0;
                }
                BasicMachineEntity.resetTicker(be);

            }
            be.neutronBombardment();
            be.distributeEnergy();

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

                if (checkTagContains(level, p1, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || checkTagContains(level, p2, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || checkTagContains(level, p3, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || checkTagContains(level, p4, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || checkTagContains(level, p5, ModTags.Blocks.FISSION_REACTOR_WALL)
                        || checkTagContains(level, p6, ModTags.Blocks.FISSION_REACTOR_WALL)
                ) return false;

            }

        }

        return true;
    }

    private final Queue<Direction> directionQueue = Queues.newArrayDeque(Direction.Plane.HORIZONTAL);

    private void distributeEnergy() {
        if (this.energy <= 0) {
            return;
        }
        this.directionQueue.offer(this.directionQueue.remove());
        for (Direction dir : directionQueue) {
            BlockEntity be = this.getLevel().getBlockEntity(this.getBlockPos().offset(dir.getNormal()));
            if (be != null) {
                be.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(e -> {
                    if (e.canReceive()) {
                        int diff = e.receiveEnergy(Math.min(MAX_TRANSFER, this.energy), false);
                        if (diff != 0) {
                            this.energy -= diff;
                            this.inventoryChanged();
                        }
                    }
                });
            }

        }
    }

    void neutronBombardment() {
        ItemStack stack = this.invItemStackHandler.getStackInSlot(0);
        if (stack != null && stack.is(ModTags.Items.NEUTRON_CONTAINER)) {
            if (this.neutron > 0) {
                CompoundTag compoundtag = stack.getOrCreateTag();
                int count = compoundtag.getInt(NEUTRON_TAG);
                if (count < MAX_NEUTRON_FOR_ITEM) {
                    compoundtag.putInt(NEUTRON_TAG, ++count);
                    this.neutron--;

                }
                ListTag lore = new ListTag();
                CompoundTag display = new CompoundTag();
                // TODO: need to be improved in future
                StringTag text = StringTag.valueOf("{\"text\":\"Neutron: " + count + '/' + MAX_NEUTRON_FOR_ITEM + "\"}");

                display.put("Lore", lore);
                lore.add(text);
                compoundtag.put("display", display);

            }
        }

    }

    boolean checkBlockPos(Level level, BlockPos pos, Block block) {
        return level.getBlockState(pos).getBlock() == block;
    }

    boolean checkTagContains(Level level, BlockPos pos, TagKey<Block> tag) {
        return !level.getBlockState(pos).is(tag);
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
