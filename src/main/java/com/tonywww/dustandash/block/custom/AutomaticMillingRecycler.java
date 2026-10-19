package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.block.entity.AutomaticMillingRecyclerEntity;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class AutomaticMillingRecycler extends AbstractHorizontalMachineBlock<AutomaticMillingRecyclerEntity> {
    public AutomaticMillingRecycler(Properties properties) {
        super(properties, AutomaticMillingRecyclerEntity.class, AutomaticMillingRecycler::new);
    }

    @Override
    public InteractionResult onMachineUse(BlockState state, Level level, BlockPos pos, Player player,
                                          InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof AutomaticMillingRecyclerEntity recycler)
                || recycler.getConnectedMill() == null) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.dustandash.recycler.no_mill"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.onMachineUse(state, level, pos, player, hand, hit);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DAABlockEntities.AUTOMATIC_MILLING_RECYCLER_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state,
                                                                BlockEntityType<E> type) {
        return level.isClientSide ? null : createTickerHelper(type,
                DAABlockEntities.AUTOMATIC_MILLING_RECYCLER_ENTITY.get(), AutomaticMillingRecyclerEntity::tick);
    }
}
