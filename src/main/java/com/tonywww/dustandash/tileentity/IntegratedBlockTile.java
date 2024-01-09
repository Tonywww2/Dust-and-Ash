package com.tonywww.dustandash.tileentity;

import com.tonywww.dustandash.container.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.ModRecipeTypes;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class IntegratedBlockTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {

    protected AxisAlignedBB area;
    public int radius = 1;

    public final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<ItemStackHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public IntegratedBlockTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(8) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
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

    public IntegratedBlockTile() {
        this(ModTileEntities.INTEGRATED_BLOCK_TILE.get());
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null || side == Direction.DOWN) {
                return this.handler.cast();

            }
        }
        return super.getCapability(cap, side);
    }

    /**
     * get the structure level
     *
     * @return the level (int)
     */
    public int getStructureLevel() {
        int out = 0;
        if (
                this.getLevel().getBlockState(this.worldPosition.north()).getBlock() == Blocks.OBSIDIAN &&
                        this.getLevel().getBlockState(this.worldPosition.east()).getBlock() == Blocks.OBSIDIAN &&
                        this.getLevel().getBlockState(this.worldPosition.south()).getBlock() == Blocks.OBSIDIAN &&
                        this.getLevel().getBlockState(this.worldPosition.west()).getBlock() == Blocks.OBSIDIAN
        ) {
            out++;

            if (
                    this.getLevel().getBlockState(this.worldPosition.north().east()).getBlock() == Blocks.CRYING_OBSIDIAN &&
                            this.getLevel().getBlockState(this.worldPosition.north().west()).getBlock() == Blocks.CRYING_OBSIDIAN &&
                            this.getLevel().getBlockState(this.worldPosition.south().east()).getBlock() == Blocks.CRYING_OBSIDIAN &&
                            this.getLevel().getBlockState(this.worldPosition.south().west()).getBlock() == Blocks.CRYING_OBSIDIAN
            ) {
                out++;

                if (
                        this.getLevel().getBlockState(this.worldPosition.north(2)).getBlock() == Blocks.NETHERITE_BLOCK &&
                                this.getLevel().getBlockState(this.worldPosition.east(2)).getBlock() == Blocks.NETHERITE_BLOCK &&
                                this.getLevel().getBlockState(this.worldPosition.south(2)).getBlock() == Blocks.NETHERITE_BLOCK &&
                                this.getLevel().getBlockState(this.worldPosition.west(2)).getBlock() == Blocks.NETHERITE_BLOCK
                ) {
                    out++;
                }
            }
        }


        return out;
    }

    public boolean isBeaconOn() {
        if (this.getLevel().getBlockState(this.worldPosition.below(2)).getBlock() == Blocks.BEACON) {
            TileEntity tileEntity = getLevel().getBlockEntity(this.worldPosition.below(2));
            if (tileEntity instanceof BeaconTileEntity) {
                BeaconTileEntity beacon = (BeaconTileEntity) tileEntity;
                return beacon.getLevels() > 0;

            }

        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("screen.dustandash.integrated_block");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new IntegratedBlockContainer(id, playerInventory, this);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            craft();
            float chance = 0.2f;
            if (getLevel().random.nextFloat() < chance) {
                getArea(getBlockPos());
                List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, area, VALID_ITEM_ENTITY);
                for (ItemEntity item : items) {
                    for (int i = 0; i < this.itemStackHandler.getSlots(); i++) {
                        if (this.itemStackHandler.getStackInSlot(i).getCount() == 0) {
                            this.itemStackHandler.insertItem(i, item.getItem().copy(), false);
                            item.getItem().setCount(item.getItem().getCount() - 1);
                            getLevel().playSound(null, getBlockPos(), SoundEvents.CHICKEN_HURT, SoundCategory.BLOCKS, 0.25f, 1f);

                        }

                        if (item.getItem().getCount() == 0) {
                            break;
                        }
                    }

                }


            }
        }


    }

    // from thermal
    protected static final Predicate<ItemEntity> VALID_ITEM_ENTITY = item -> {
        if (!item.isAlive()) {
//        if (!item.isAlive() || item.hasPickUpDelay()) {
            return false;
        }

        return ModTags.Items.CRAFT_MATERIAL.contains(item.getItem().getItem());
    };

    public void getArea(BlockPos blockPos) {
        if (area == null) {
            area = new AxisAlignedBB(blockPos.offset(-radius, -1, -radius), blockPos.offset(1 + radius, 1 + radius, 1 + radius));
        }
    }

    public void craft() {
        Inventory inv = new Inventory(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));

        }

        Optional<IntegratedBlockRecipe> recipe = this.getLevel().getRecipeManager().getRecipeFor(ModRecipeTypes.INTEGRATE_RECIPE, inv, this.getLevel());

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();

            if (getStructureLevel() >= iRecipe.getLevel()) {
                // crafting
                for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                    itemStackHandler.extractItem(i, 1, false);

                }
                playParticles(getStructureLevel());
                ItemEntity itemEntity = new ItemEntity(
                        this.getLevel(),
                        getBlockPos().getX(),
                        getBlockPos().getY() + 1,
                        getBlockPos().getZ(),
                        output);
                this.getLevel().addFreshEntity(itemEntity);
                if (isBeaconOn()) {
                    this.getLevel().addFreshEntity(itemEntity.copy());
                }

            }

            setChanged();
        });

    }

    public void playParticles(int lv) {
        if (!this.getLevel().isClientSide) {
            switch (lv) {
                case 1:
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f);
                    break;

                case 2:
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f);
                    break;

                case 3:
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1f, 1f);
                    break;

                default:
                    this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);
                    break;
            }

        }
    }

}
