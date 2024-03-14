package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Random;

import static com.tonywww.dustandash.config.DustAndAshConfig.*;

public class DustSource extends Block {

    private double chancePerTick = dustSourceChancePerTick.get();
    private double chancePerBlock = dustSourceChancePerBlock.get();
    private int height = dustSourceHeight.get();
    private int radius = dustSourceRadius.get();

    public DustSource(Properties properties) {
        super(properties);

    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, Random random) {

        float chance = 0.3f;

        if (random.nextDouble() < chance) {
            world.addParticle(
                    ParticleTypes.SMOKE,
                    blockPos.getX() + random.nextDouble(),
                    blockPos.getY() + 1.1d,
                    blockPos.getZ() + random.nextDouble(),
                    (random.nextDouble() - 0.05d) * 0.2,
                    -0.075d,
                    (random.nextDouble() - 0.50d) * 0.2
            );

        }

        super.animateTick(blockState, world, blockPos, random);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, Random random) {
        if (random.nextDouble() < chancePerTick) {
            dustCycle(world, blockPos, random, chancePerBlock, height, radius);

        }

        super.randomTick(blockState, world, blockPos, random);

    }

    private static void dustCycle(ServerLevel world, BlockPos blockPos, Random random, double chancePerBlock, int height, int radius) {
        //1.18+ Need to check below 0
        int startX = blockPos.getX() - radius;
        int startZ = blockPos.getZ() - radius;
        int startY = blockPos.getY() - height;

        int endX = blockPos.getX() + radius;
        int endZ = blockPos.getZ() + radius;
        int endY = blockPos.getY();

        if (startY < world.getMinBuildHeight()) startY = world.getMinBuildHeight();

        //y
        for (int y = startY; y < endY; y++) {
            //x
            for (int x = startX; x <= endX; x++) {
                //z
                for (int z = startZ; z <= endZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockPos currentUpperPos = new BlockPos(x, y + 1, z);

                    BlockState currentBlock = world.getBlockState(currentPos);
                    BlockState currentUpperBlock = world.getBlockState(currentUpperPos);

                    if (currentUpperBlock.is(Blocks.AIR) && currentBlock.is(ModTags.Blocks.DUST_ABLE) && !currentBlock.is(ModTags.Blocks.NOT_DUST_ABLE)) {
                        if (random.nextDouble() < chancePerBlock) {
                            world.setBlock(currentUpperPos, ModBlocks.DUST.get().defaultBlockState(), 2);
                            world.updateNeighborsAt(currentUpperPos, ModBlocks.DUST.get());

                        }

                    }


                }

            }


        }
    }

}
