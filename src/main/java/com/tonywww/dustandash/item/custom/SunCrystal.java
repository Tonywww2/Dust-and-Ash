package com.tonywww.dustandash.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraft.world.storage.ServerWorldInfo;

import java.util.Objects;

public class SunCrystal extends Item {
    public SunCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {

        World world = context.getLevel();

        if (!world.isClientSide) {
            PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());
            IWorldInfo data = world.getLevelData();

            data.setRaining(false);
            int time = world.random.nextInt(world.getLevelData().isRaining() ? 12000 : 168000) + 12000;

            if (data instanceof ServerWorldInfo) {
                ((ServerWorldInfo) data).setRainTime(time);
            }

            playerEntity.getCooldowns().addCooldown(getItem(), 200);
            stack.setCount(stack.getCount() - 1);

        }

        return super.onItemUseFirst(stack, context);
    }
}
