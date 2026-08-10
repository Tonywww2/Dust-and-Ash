package com.tonywww.dustandash.block.custom.FissionReactor;

import com.tonywww.dustandash.block.custom.AbstractHorizontalMachineBlock;
import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
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

public class FissionReactorController extends AbstractHorizontalMachineBlock<FissionReactorControllerEntity> {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public FissionReactorController(Properties properties) {
        super(properties, FissionReactorControllerEntity.class);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DAABlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state,
                                                                   BlockEntityType<E> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, DAABlockEntities.FISSION_REACTOR_CONTROLLER_ENTITY.get(),
                FissionReactorControllerEntity::tick);
    }
}