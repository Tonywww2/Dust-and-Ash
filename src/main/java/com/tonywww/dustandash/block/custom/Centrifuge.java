package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.block.entity.CentrifugeEntity;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Centrifuge extends AbstractHorizontalMachineBlock<CentrifugeEntity> {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public Centrifuge(Properties properties) {
        super(properties, CentrifugeEntity.class);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DAABlockEntities.CENTRIFUGE_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state,
                                                                   BlockEntityType<E> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, DAABlockEntities.CENTRIFUGE_ENTITY.get(), CentrifugeEntity::tick);
    }
}