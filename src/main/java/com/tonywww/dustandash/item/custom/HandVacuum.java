package com.tonywww.dustandash.item.custom;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.loottables.ModLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.loot.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Objects;

public class HandVacuum extends Item {
    public HandVacuum(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();

        if (!world.isClientSide) {
            PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());

            if (changeBlock(context, playerEntity, context.getLevel())) {
                stack.hurtAndBreak(1, playerEntity, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

            }


        }

        return super.onItemUseFirst(stack, context);
    }

    private boolean changeBlock(ItemUseContext context, PlayerEntity playerEntity, World world) {

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState1 = Blocks.AIR.defaultBlockState();
        BlockState blockState2 = world.getBlockState(blockPos);

        if (blockState2.getBlock().equals(ModBlocks.DUST.get()) && !playerEntity.getCooldowns().isOnCooldown(getItem())) {

            playerEntity.getCooldowns().addCooldown(getItem(), 15);
            //successful rate
            if (random.nextFloat() > 0.45f) {
                //failed
                world.playSound(null, blockPos, SoundEvents.CHICKEN_HURT, SoundCategory.BLOCKS, 1f, 1f);
                return false;

            } else {
                //succeed
                world.setBlock(blockPos, blockState1, 2);
                retrieve(blockPos, world);
                world.playSound(null, blockPos, SoundEvents.IRON_GOLEM_HURT, SoundCategory.BLOCKS, 1f, 1f);
                return true;

            }

        }

        return false;

    }

    private void retrieve(BlockPos blockPos, World world){
        LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).withRandom(Item.random);
        //Different
        LootTable lootTable = world.getServer().getLootTables().get(ModLootTables.HAND);
        List<ItemStack> list = lootTable.getRandomItems(builder.create(LootParameterSets.EMPTY));

        for (ItemStack i : list){
            ItemEntity itemEntity = new ItemEntity(
                    world,
                    blockPos.getX() + 0.5f,
                    blockPos.getY() + 0.5f,
                    blockPos.getZ() + 0.5f,
                    i
            );
            world.addFreshEntity(itemEntity);

        }

    }


}
