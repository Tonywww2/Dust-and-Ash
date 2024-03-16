package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.loottables.ModLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class HandVacuum extends Item {

    private double successRate;

    public HandVacuum(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();

        if (!world.isClientSide) {
            Player playerEntity = Objects.requireNonNull(context.getPlayer());

            if (changeBlock(context, playerEntity, context.getLevel())) {
                stack.hurtAndBreak(1, playerEntity, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

            }


        }

        return super.onItemUseFirst(stack, context);
    }

    private boolean changeBlock(UseOnContext context, Player playerEntity, Level world) {

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (blockState.getBlock().equals(ModBlocks.DUST.get()) && !playerEntity.getCooldowns().isOnCooldown(this)) {

            successRate = DustAndAshConfig.handVacuumSuccessRate.get();

            //successful rate
            if (world.random.nextDouble() < successRate) {
                //succeed
                playerEntity.getCooldowns().addCooldown(this, 15);
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                retrieve(blockPos, world, playerEntity);

                world.playSound(null, blockPos, SoundEvents.IRON_GOLEM_HURT, SoundSource.BLOCKS, 1f, 1f);
                return true;


            } else {
                //failed
                playerEntity.getCooldowns().addCooldown(this, 10);
                world.playSound(null, blockPos, SoundEvents.CHICKEN_HURT, SoundSource.BLOCKS, 1f, 1f);
                return false;
            }

        }

        return false;

    }


    private void retrieve(BlockPos blockPos, Level level, Player playerEntity) {
        LootParams lootparams = (new LootParams.Builder((ServerLevel) level))
                .withLuck(playerEntity.getLuck())
                .create(LootContextParamSets.EMPTY);

        LootTable lootTable = level.getServer().getLootData().getLootTable(ModLootTables.HAND_VACUUM);
        List<ItemStack> list = lootTable.getRandomItems(lootparams);

        for (ItemStack i : list) {
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    blockPos.getX() + 0.5f,
                    blockPos.getY() + 0.5f,
                    blockPos.getZ() + 0.5f,
                    i
            );
            level.addFreshEntity(itemEntity);

        }

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.hand_vacuum"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }


}
