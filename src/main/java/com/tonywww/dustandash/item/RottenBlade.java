package com.tonywww.dustandash.item;

import com.tonywww.dustandash.block.entity.FissionReactor.FissionReactorControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Objects;

public class RottenBlade extends SwordItem {
    public RottenBlade(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();

        if (!world.isClientSide) {
            BlockPos blockPos = context.getClickedPos();
            Player playerEntity = Objects.requireNonNull(context.getPlayer());

            if (world.getBlockEntity(blockPos) instanceof FissionReactorControllerEntity fissionReactorControllerEntity) {
                fissionReactorControllerEntity.getCapability(ForgeCapabilities.ENERGY, context.getClickedFace()).ifPresent( e -> {
                    playerEntity.sendSystemMessage(Component.literal(String.valueOf(e.getEnergyStored())));

                });
            }

        }
        return super.onItemUseFirst(stack, context);
    }
}
