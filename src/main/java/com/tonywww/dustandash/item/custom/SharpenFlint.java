package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SharpenFlint extends Item {
    public SharpenFlint(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level world = context.getLevel();

        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand pHand) {
        if (!world.isClientSide) {
            ItemStack item = player.getMainHandItem();
            if (item.getItem() == this && player.invulnerableTime <= 0 && item.getCount() > 0) {
                //TODO ModDamageSources
                player.hurt(world.damageSources().magic(), 2.0f);
                world.addFreshEntity(new ItemEntity(
                        world,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        ModItems.BLOODY_FLINT.get().getDefaultInstance()
                ));
                item.shrink(1);


            }


        }

        return super.use(world, player, pHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.sharpen_flint"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
