package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.util.ModDamageSource;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Objects;

public class SharpenFlint extends Item {
    public SharpenFlint(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {

        World world = context.getLevel();


        return super.onItemUseFirst(stack, context);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand pHand) {
        if (!world.isClientSide) {
            ItemStack item = player.getMainHandItem();
            if (item.getItem() == this && player.invulnerableTime <= 0 && item.getCount() > 0) {
                player.hurt(ModDamageSource.SHARPEN_FLINT, 2.0f);
                world.addFreshEntity(new ItemEntity(
                        world,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        ModItems.BLOODY_FLINT.get().getDefaultInstance()
                ));
                item.setCount(item.getCount() - 1);


            }


        }

        return super.use(world, player, pHand);
    }
}
