package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.container.AshCollectorContainer;
import com.tonywww.dustandash.container.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipeTypes;
import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HopperBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AshCollectorTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    public final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> itemStackHandler);
    private int coolDownTime = -1;

    public AshCollectorTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (slot == 0 && stack.getItem() == ModItems.ASH.get()) ||
                        (slot == 1);
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    public AshCollectorTile() {
        this(ModTileEntities.ASH_COLLECTOR_TILE.get());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.handler.cast();

        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        itemStackHandler.deserializeNBT(compoundNBT.getCompound("inv"));
        super.load(blockState, compoundNBT);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", itemStackHandler.serializeNBT());
        return super.save(compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.dustandash.ash_collector");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new AshCollectorContainer(id, playerInventory, this);
    }

    @Override
    public void tick() {
        float chance = 0.002f;
        if (this.level != null && !this.level.isClientSide) {
            suckInItem();

            if (getLevel().random.nextFloat() < chance) {
                if (shouldWork()) {
                    ItemStack itemStack = new ItemStack(ModItems.ASH.get());
                    this.itemStackHandler.insertItem(0, itemStack.copy(), false);
                    setChanged();

                }

            }
        }
        this.coolDownTime--;

    }

    public boolean shouldWork() {
        if (getLevel().getBlockState(this.worldPosition.above()).getBlock() instanceof AbstractFurnaceBlock) {
            BlockState block = getLevel().getBlockState(this.worldPosition.above());

            return block.getValue(BlockStateProperties.LIT);

        }
        return false;

    }

    private void suckInItem() {
        if (!isOnCoolDown() && this.itemStackHandler.getStackInSlot(1).getCount() < this.itemStackHandler.getSlotLimit(1)) {
            if (getLevel().getBlockEntity(this.worldPosition.above()) instanceof AbstractFurnaceTileEntity) {
                AbstractFurnaceTileEntity tile = (AbstractFurnaceTileEntity) getLevel().getBlockEntity(this.worldPosition.above());
                this.itemStackHandler.insertItem(1, tile.removeItem(2, 1), false);
                setCoolDown(4);


            }
        }

    }


    public void setCoolDown(int cd) {
        this.coolDownTime = cd;
    }

    private boolean isOnCoolDown() {
        return this.coolDownTime > 0;
    }


}
