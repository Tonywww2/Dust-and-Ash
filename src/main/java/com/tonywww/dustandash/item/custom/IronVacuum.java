package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.loottables.ModLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


public class IronVacuum extends Item {

    private double consumeRate;

    public IronVacuum(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();

        if (!world.isClientSide) {
            Player playerEntity = Objects.requireNonNull(context.getPlayer());

            if (!playerEntity.getCooldowns().isOnCooldown(this)) {
                if (changeBlock(context, playerEntity, context.getLevel())) {
                    stack.hurtAndBreak(1, playerEntity, player -> player.broadcastBreakEvent(player.getUsedItemHand()));
                    world.playSound(null, context.getClickedPos(), SoundEvents.ZOMBIE_VILLAGER_HURT, SoundSource.BLOCKS, 1f, 1f);

                } else {
                    world.playSound(null, context.getClickedPos(), SoundEvents.SKELETON_HURT, SoundSource.BLOCKS, 1f, 1f);

                }

            }


        }

        return super.onItemUseFirst(stack, context);
    }

    private boolean changeBlock(UseOnContext context, Player playerEntity, Level world) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);

        consumeRate = DustAndAshConfig.ironVacuumConsumeRate.get();

        if (blockState.getBlock().equals(ModBlocks.DUST.get())) {
            for (ItemStack i : playerEntity.inventoryMenu.getItems()) {
                if (i.getItem() == ModItems.DUST_WITH_ENERGY.get() && i.getCount() > 0) {
                    // going to cost
                    if (world.random.nextDouble() < consumeRate) {
                        i.setCount(i.getCount() - 1);

                    }
                    playerEntity.getCooldowns().addCooldown(this, 5);
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                    retrieve(blockPos, world, playerEntity);
                    return true;

                }

            }
            playerEntity.getCooldowns().addCooldown(this, 5);
            return false;

        }

        return false;

    }

    private void retrieve(BlockPos blockPos, Level level, Player playerEntity) {
        LootParams lootparams = (new LootParams.Builder((ServerLevel) level))
                .withLuck(playerEntity.getLuck())
                .create(LootContextParamSets.EMPTY);
        //Different
        LootTable lootTable = level.getServer().getLootData().getLootTable(ModLootTables.IRON_VACUUM);
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
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return super.isValidRepairItem(pToRepair, pRepair) || Tiers.IRON.getRepairIngredient().test(pRepair);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.iron_vacuum"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }


}
