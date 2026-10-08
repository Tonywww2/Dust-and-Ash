package com.tonywww.dustandash.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import java.util.List;
import java.util.function.Function;

public abstract class PlatformMachineBlock extends BaseEntityBlock {
    //? if neoforge {
    /*private final com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec;
    *///?}
    protected PlatformMachineBlock(Properties properties, Function<Properties, ? extends PlatformMachineBlock> factory) {
        super(properties);
        //? if neoforge {
        /*this.codec = simpleCodec(factory);
        *///?}
    }
    //? if forge {
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return onMachineUse(state, level, pos, player, hand, hit);
    }
    @Override public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        appendTooltip(stack, level, tooltip, flag);
    }
    //?} else {
    /*@Override protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() { return codec; }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return onMachineUse(state, level, pos, player, InteractionHand.MAIN_HAND, hit);
    }
    @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        appendTooltip(stack, context.level(), tooltip, flag);
    }
    *///?}
    protected abstract InteractionResult onMachineUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit);
    public void appendTooltip(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {}
}
