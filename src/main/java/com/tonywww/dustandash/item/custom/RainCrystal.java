package com.tonywww.dustandash.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class RainCrystal extends Item {
    public RainCrystal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand pHand) {
        if (!world.isClientSide) {
            ItemStack stack = null;
            if (player.getMainHandItem().getItem() == this) {
                stack = player.getMainHandItem();

            } else if (player.getOffhandItem().getItem() == this) {
                stack = player.getOffhandItem();

            }
            if (stack != null) {
                player.getCooldowns().addCooldown(this, 200);
                world.getLevelData().setRaining(true);
                stack.setCount(stack.getCount() - 1);

            }

        }

        return super.use(world, player, pHand);
    }

    @Override
    public void onUseTick(Level world, LivingEntity entity, ItemStack stack, int pCount) {

        super.onUseTick(world, entity, stack, pCount);
    }
}
