package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.tileentity.IntegratedBlockTile;
import com.tonywww.dustandash.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


public class IntegratedBlock extends Block {

    public IntegratedBlock(Properties properties) {
        super(properties);
//        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));

    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {

        if (!pLevel.isClientSide) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);

            if (tileEntity instanceof IntegratedBlockTile) {
                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, (IntegratedBlockTile) tileEntity, pPos);
            } else {
                throw new IllegalStateException("Container provider is missing");
            }

        }

        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.INTEGRATED_BLOCK_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof IntegratedBlockTile) {
                IntegratedBlockTile tile = (IntegratedBlockTile) tileentity;
                Inventory inv = new Inventory(tile.itemStackHandler.getSlots());

                for (int i = 0; i < tile.itemStackHandler.getSlots(); i++) {
                    inv.setItem(i, tile.itemStackHandler.getStackInSlot(i));

                }
                InventoryHelper.dropContents(pLevel, pPos, inv);
                pLevel.updateNeighbourForOutputSignal(pPos, this);

            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }
}
