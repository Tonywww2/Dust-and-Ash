package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.menu.ItemSenderContainerMenu;
import com.tonywww.dustandash.menu.itemhandlers.RestrictedItemHandler;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

public class ItemSenderEntity extends BasicMachineEntity implements MenuProvider, DroppableInventory {
    public static final int TARGET_SLOT_COUNT = 4;

    public ItemStackHandler invItemStackHandler;
    private final ManagedCapability<ItemStackHandler> handler;
    private final ManagedCapability<RestrictedItemHandler> normalHandler;
    private int[] targetSlots;
    @Nullable
    private BlockPos targetPos;
    protected final ContainerData dataAccess;

    public ItemSenderEntity(BlockPos pos, BlockState state) {
        super(DAABlockEntities.ITEM_SENDER_ENTITY.get(), pos, state);

        this.invItemStackHandler = createHandler();
        this.handler = managedCapability(() -> invItemStackHandler);
        this.normalHandler = managedCapability(() -> new RestrictedItemHandler(invItemStackHandler,
            slot -> slot != 0, slot -> slot != 0));

        this.targetSlots = new int[TARGET_SLOT_COUNT];
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int index) {
                if (targetPos != null) {
                    switch (index) {
                        case 0 -> {
                            return targetPos.getX();
                        }
                        case 1 -> {
                            return targetPos.getY();
                        }
                        case 2 -> {
                            return targetPos.getZ();
                        }
                    }

                }
                return Integer.MIN_VALUE;
            }

            @Override
            public void set(int index, int val) {
                if (targetPos == null) {
                    return;
                }
                switch (index) {
                    case 0 -> targetPos = new BlockPos(val, targetPos.getY(), targetPos.getZ());
                    case 1 -> targetPos = new BlockPos(targetPos.getX(), val, targetPos.getZ());
                    case 2 -> targetPos = new BlockPos(targetPos.getX(), targetPos.getY(), val);
                }

            }

            @Override
            public int getCount() {
                return 3;
            }
        };

    }

    // 0 position selector
    // 1-4 sending buffer
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return slot != 0 || stack.is(DAAItems.POSITION_SELECTOR.get());
            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot == 0) {
                    return 1;
                }
                return 64;
            }


        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("screen.dustandash.item_sender");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ItemSenderContainerMenu(id, playerInventory, this, dataAccess);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        invItemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));
        int[] savedTargetSlots = compoundNBT.getIntArray("target_slots");
        targetSlots = savedTargetSlots.length == TARGET_SLOT_COUNT
            ? Arrays.copyOf(savedTargetSlots, TARGET_SLOT_COUNT)
            : new int[TARGET_SLOT_COUNT];
        super.load(compoundNBT);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.put("inv", invItemStackHandler.serializeNBT());
        compound.putIntArray("target_slots", targetSlots);
        super.saveAdditional(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.handler.get().cast();
            }
            return this.normalHandler.get().cast();

        }
        return super.getCapability(cap, side);
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.invItemStackHandler;
    }

    public void setTargetSlots(byte[] arr) {
        if (arr.length != TARGET_SLOT_COUNT) {
            throw new IllegalArgumentException("Item sender requires exactly " + TARGET_SLOT_COUNT + " target slots");
        }
        for (int i = 0; i < TARGET_SLOT_COUNT; i++) {
            this.targetSlots[i] = arr[i];
        }
        setChanged();

    }

    public int[] getTargetSlots() {
        return Arrays.copyOf(targetSlots, targetSlots.length);
    }

    private static BlockPos arrToBlockPos(int[] arr) {
        if (arr != null && arr.length >= 3) {
            return new BlockPos(arr[0], arr[1], arr[2]);
        }
        return null;

    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemSenderEntity be) {

        if (!level.isClientSide()) {
            if (be.advanceWorkCycle(1)) {

                // get target pos
                ItemStack positionSelector = be.invItemStackHandler.getStackInSlot(0);
                if (!positionSelector.isEmpty()) {
                    CompoundTag compoundtag = positionSelector.getOrCreateTag();
                    int[] itemPos = compoundtag.getIntArray("position");
                    be.targetPos = arrToBlockPos(itemPos);

                } else {
                    be.targetPos = null;
                }

                // send item
                if (be.targetPos != null) {
                    Container container = getContainerAt(level, be.targetPos);

                    if (container != null) {
                        for (int i = 0; i < 4; i++) {
                            int targetSlot = be.targetSlots[i];
                            ItemStack thisStack = be.invItemStackHandler.getStackInSlot(i + 1);

                            if (!thisStack.is(DAAItems.PLACEHOLDER.get())) {
                                if (targetSlot >= 0 && container.getContainerSize() > targetSlot) {
                                    ItemStack targetStack = container.getItem(targetSlot);

                                    if (container.canPlaceItem(targetSlot, thisStack) ||
                                            (container instanceof WorldlyContainer worldlyContainer
                                                    && worldlyContainer.canPlaceItemThroughFace(targetSlot, thisStack, null))) {
                                        if (canMoveOneItem(container, targetStack, thisStack)) {
                                            if (targetStack.isEmpty()) {
                                                ItemStack moved = thisStack.copy();
                                                moved.setCount(1);
                                                container.setItem(targetSlot, moved);
                                            } else {
                                                targetStack.grow(1);
                                            }
                                            thisStack.shrink(1);
                                            container.setChanged();
                                            be.inventoryChanged();
                                        }
                                    }
                                }
                            }

                        }

                    } else {
                        getItemHandler(level, be.targetPos).map(result -> {
                            IItemHandler handler = result.getKey();
                            boolean insertedItem = false;

                            for (int i = 0; i < 4; i++) {
                                int targetSlot = be.targetSlots[i];
                                if (targetSlot >= 0 && targetSlot < handler.getSlots()) {
                                    ItemStack thisStack = be.invItemStackHandler.getStackInSlot(i + 1);
                                    if (!thisStack.isEmpty()) {
                                        ItemStack offered = thisStack.copy();
                                        offered.setCount(1);
                                        ItemStack remainder = handler.insertItem(targetSlot, offered, false);
                                        if (remainder.isEmpty()) {
                                            thisStack.shrink(1);
                                            insertedItem = true;
                                        }

                                    }
                                }
                            }
                            return insertedItem;

                        });

                    }

                }
                be.resetWorkCycle();
            }


        }

    }

    private static boolean canMoveOneItem(Container container, ItemStack targetStack, ItemStack sourceStack) {
        if (sourceStack.isEmpty()) {
            return false;
        }
        if (targetStack.isEmpty()) {
            return Math.min(container.getMaxStackSize(), sourceStack.getMaxStackSize()) > 0;
        }
        if (!ItemStack.isSameItemSameTags(targetStack, sourceStack)) {
            return false;
        }
        int maxStackSize = Math.min(container.getMaxStackSize(), targetStack.getMaxStackSize());
        return targetStack.getCount() < maxStackSize;
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(Level worldIn, BlockPos blockpos) {
        net.minecraft.world.level.block.state.BlockState state = worldIn.getBlockState(blockpos);

        if (state.hasBlockEntity()) {
            BlockEntity blockEntity = worldIn.getBlockEntity(blockpos);
            if (blockEntity != null) {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                        .map(capability -> ImmutablePair.<IItemHandler, Object>of(capability, blockEntity));
            }
        }

        return Optional.empty();
    }

    @Nullable
    private static Container getContainerAt(Level level, BlockPos blockPos) {
        Container container = null;
        BlockState blockstate = level.getBlockState(blockPos);
        Block block = blockstate.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            container = ((WorldlyContainerHolder) block).getContainer(blockstate, level, blockPos);
        } else if (blockstate.hasBlockEntity()) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof Container) {
                container = (Container) blockentity;
                if (container instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    container = ChestBlock.getContainer((ChestBlock) block, blockstate, level, blockPos, true);
                }
            }
        }

        return container;
    }
}
