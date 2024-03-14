package com.tonywww.dustandash.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;

public class SunCrystal extends Item {
    public SunCrystal(Properties pProperties) {
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
                LevelData data = world.getLevelData();
                data.setRaining(false);
                int time = world.random.nextInt(world.getLevelData().isRaining() ? 12000 : 168000) + 12000;
                if (data instanceof ServerLevelData) {
                    ((ServerLevelData) data).setRainTime(time);
                }
                player.getCooldowns().addCooldown(this, 200);
                stack.setCount(stack.getCount() - 1);

            }

        }

        return super.use(world, player, pHand);
    }
}
