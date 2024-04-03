package com.tonywww.dustandash.block.entity;

import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.menu.ItemSenderContainerMenu;
import com.tonywww.dustandash.menu.ItemSenderItemHandler;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ItemSenderEntity extends SyncedBlockEntity implements MenuProvider {
    public ItemStackHandler invItemStackHandler;
    private final LazyOptional<ItemStackHandler> handler;
    private final LazyOptional<ItemSenderItemHandler> normalHandler;
    private int[] targetSlots;
    @Nullable
    private BlockPos targetPos;

    private final int GOAL_TICK = 4;
    private int tickCount;
    protected final ContainerData dataAccess;

    public ItemSenderEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ITEM_SENDER_ENTITY.get(), pos, state);

        this.invItemStackHandler = createHandler();
        this.handler = LazyOptional.of(() -> invItemStackHandler);
        this.normalHandler = LazyOptional.of(() -> new ItemSenderItemHandler(invItemStackHandler, null));

        this.targetSlots = new int[4];
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
                switch (index) {
                    case 0:
                        targetPos.offset(val - targetPos.getX(), 0, 0);

                    case 1:
                        targetPos.offset(0, val - targetPos.getY(), 0);

                    case 2:
                        targetPos.offset(0, 0, val - targetPos.getZ());

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
                return slot != 0 || stack.is(ModItems.POSITION_SELECTOR.get());
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
        targetSlots = compoundNBT.getIntArray("target_slots");
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
                return this.handler.cast();
            }
            return this.normalHandler.cast();

        }
        return super.getCapability(cap, side);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < invItemStackHandler.getSlots(); ++i) {
            drops.add(invItemStackHandler.getStackInSlot(i));
        }
        return drops;
    }

    public void setTargetSlots(byte[] arr) {
        for (int i = 0; i < this.targetSlots.length; i++) {
            this.targetSlots[i] = arr[i];

        }

    }

    public int[] getTargetSlots() {
        return targetSlots;
    }

    @Nullable
    public BlockPos getTargetPos() {
        return targetPos;
    }

    private static BlockPos arrToBlockPos(int[] arr) {
        return new BlockPos(arr[0], arr[1], arr[2]);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ItemSenderEntity be) {

        if (!level.isClientSide()) {
            if (be.tickCount < be.GOAL_TICK) {
                be.tickCount++;
            } else {
                // reset
                be.tickCount = 0;

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

                            if (!thisStack.is(ModItems.PLACEHOLDER.get())) {
                                if (container.getContainerSize() > targetSlot) {
                                    ItemStack targetStack = container.getItem(targetSlot);

                                    if (container.canPlaceItem(targetSlot, thisStack) ||
                                            (container instanceof WorldlyContainer worldlyContainer)
                                                    && worldlyContainer.canPlaceItemThroughFace(targetSlot, thisStack, null)) {
                                        if (targetStack.isEmpty()) {
                                            container.setItem(targetSlot, thisStack.split(1));

                                        } else if (targetStack.is(thisStack.getItem())) {
                                            targetStack.grow(1);
                                            thisStack.shrink(1);

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
                                if (targetSlot < handler.getSlots()) {
                                    ItemStack thisStack = be.invItemStackHandler.getStackInSlot(i + 1);
                                    ItemStack targetStack = handler.getStackInSlot(targetSlot);

                                    if (targetStack.isEmpty()) {
                                        handler.insertItem(targetSlot, thisStack.split(1), false);
                                        insertedItem = true;

                                    } else if (ItemHandlerHelper.canItemStacksStack(targetStack, thisStack)) {
//                                        int originalSize = thisStack.getCount();
                                        ItemStack newStack = handler.insertItem(targetSlot, thisStack.split(1), false);
                                        if (newStack.getCount() >= 1) {
                                            if (thisStack.isEmpty()) {
                                                be.invItemStackHandler.setStackInSlot(i + 1, newStack);

                                            } else {
                                                thisStack.grow(1);

                                            }
                                        } else {
                                            insertedItem = true;

                                        }


                                    }
                                }
                            }
                            return insertedItem;

                        });

                    }

                }

            }


        }

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
