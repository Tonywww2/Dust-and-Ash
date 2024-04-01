package com.tonywww.dustandash.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
}
