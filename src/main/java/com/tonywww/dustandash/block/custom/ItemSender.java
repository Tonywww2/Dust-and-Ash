package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.block.entity.ItemSenderEntity;
import com.tonywww.dustandash.registry.DAABlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import java.util.List;

public class ItemSender extends AbstractHorizontalMachineBlock<ItemSenderEntity> {
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 13, 15);

    public ItemSender(Properties properties) {
        super(properties, ItemSenderEntity.class);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return DAABlockEntities.ITEM_SENDER_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state,
                                                                   BlockEntityType<E> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(blockEntityType, DAABlockEntities.ITEM_SENDER_ENTITY.get(), ItemSenderEntity::tick);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dustandash.item_sender"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}