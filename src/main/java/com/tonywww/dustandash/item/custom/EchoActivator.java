package com.tonywww.dustandash.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class EchoActivator extends Item {
    public EchoActivator(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide) {
            Player playerEntity = Objects.requireNonNull(context.getPlayer());

            BlockPos blockPos = context.getClickedPos();
            BlockState blockState = level.getBlockState(blockPos);

            if (blockState.getBlock() == Blocks.SCULK_SHRIEKER) {
                if(!blockState.getValue(BlockStateProperties.CAN_SUMMON)) {
                    level.setBlock(blockPos, blockState.setValue(BlockStateProperties.CAN_SUMMON, Boolean.valueOf(true)), 3);
                    stack.hurtAndBreak(1, playerEntity, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

                }

            } else if (blockState.getBlock() == Blocks.CAKE) {
                level.setBlock(blockPos, Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(BlockStateProperties.CAN_SUMMON, Boolean.valueOf(true)), 3);
                stack.hurtAndBreak(1, playerEntity, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

            }

        }

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.echo_activator"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
