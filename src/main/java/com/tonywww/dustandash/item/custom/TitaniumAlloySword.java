package com.tonywww.dustandash.item.custom;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TitaniumAlloySword extends SwordItem {
    public TitaniumAlloySword(IItemTier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (entity instanceof LivingEntity) {
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1));
            player.addEffect(new EffectInstance(Effects.LUCK, 100, 1));
            player.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 100, 0));

            ((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 0));

        }


        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {

        pTooltip.add(new TranslationTextComponent("tooltip.dustandash.titanium_alloy_sword"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

}
