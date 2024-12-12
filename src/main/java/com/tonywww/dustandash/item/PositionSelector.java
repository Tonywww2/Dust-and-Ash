package com.tonywww.dustandash.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PositionSelector extends Item {
    public PositionSelector(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide()) {
            CompoundTag compoundtag = stack.getOrCreateTag();
            int x = context.getClickedPos().getX();
            int y = context.getClickedPos().getY();
            int z = context.getClickedPos().getZ();

            int[] arr = new int[]{x, y, z};

            compoundtag.putIntArray("position", arr);

            context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 10);

        }

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        if (pLevel != null && pLevel.isClientSide()) {
            CompoundTag compoundtag = pStack.getTag();
            if (compoundtag != null) {
                int[] arr = compoundtag.getIntArray("position");
                if (arr != null && arr.length >= 3) {
                    pTooltip.add(Component.literal(arr[0] + ", " + arr[1] + ", " + arr[2]));
                    pTooltip.add(pLevel.getBlockState(new BlockPos(arr[0], arr[1], arr[2])).getBlock().getName());

                }

            }

        }

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
