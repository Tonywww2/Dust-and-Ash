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

public class TitaniumAlloyGreatSword extends SwordItem {
    public TitaniumAlloyGreatSword(IItemTier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (entity instanceof LivingEntity) {
            player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 1));
            ((LivingEntity) entity).addEffect(new EffectInstance(Effects.WEAKNESS, 100, 0));

        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {

        pTooltip.add(new TranslationTextComponent("tooltip.dustandash.titanium_alloy_great_sword"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
