package com.tonywww.dustandash.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import java.util.Objects;

public class RainCrystal extends Item {
    public RainCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {

        World world = context.getLevel();

        if (!world.isClientSide) {
            PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());

            world.getLevelData().setRaining(true);
            playerEntity.getCooldowns().addCooldown(getItem(), 200);
            stack.setCount(stack.getCount() - 1);

        }

        return super.onItemUseFirst(stack, context);
    }
}
