package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.tileentity.IonizerTile;
import com.tonywww.dustandash.tileentity.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


public class Ionizer extends Block {

    public Ionizer(Properties properties) {
        super(properties);

    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {

        if (!pLevel.isClientSide) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);

            if (tileEntity instanceof IonizerTile) {
                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, (IonizerTile) tileEntity, pPos);
            } else {
                throw new IllegalStateException("Container provider is missing");
            }

        }

        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.IONIZER_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof IonizerTile) {
                IonizerTile tile = (IonizerTile) tileentity;
                Inventory inv = new Inventory(tile.invItemStackHandler.getSlots());

                for (int i = 0; i < tile.invItemStackHandler.getSlots(); i++) {
                    inv.setItem(i, tile.invItemStackHandler.getStackInSlot(i));

                }
                InventoryHelper.dropContents(pLevel, pPos, inv);
                pLevel.updateNeighbourForOutputSignal(pPos, this);

            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }
}
