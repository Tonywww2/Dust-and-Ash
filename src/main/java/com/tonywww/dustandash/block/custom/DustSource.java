package com.tonywww.dustandash.block.custom;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DustSource extends Block {

    public DustSource(Properties properties) {
        super(properties);

    }

    @Override
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random random) {

        float chance = 0.3f;

        if (random.nextFloat() < chance) {
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
    public void randomTick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random) {

//        float chance = 0.5f;
        int radius = 3;
        int height = 5;

        if (random.nextInt(2) == 0) {

            //1.18+ Need to check below 0
            int startX = blockPos.getX() - radius;
            int startZ = blockPos.getZ() - radius;
            int startY = blockPos.getY() - height;

            int endX = blockPos.getX() + radius;
            int endZ = blockPos.getZ() + radius;
            int endY = blockPos.getY();

            if (startY < 1) startY = 1;

            //y
            for (int y = startY; y < endY; y++) {
                //x
                for (int x = startX; x <= endX; x++) {
                    //z
                    for (int z = startZ; z <= endZ; z++) {
                        BlockPos currentPos = new BlockPos(x, y, z);
                        BlockPos currentUpperPos = new BlockPos(x, y + 1, z);

                        Block currentBlock = world.getBlockState(currentPos).getBlock();
                        Block currentUpperBlock = world.getBlockState(currentUpperPos).getBlock();

                        if (currentUpperBlock.equals(Blocks.AIR) && currentBlock.is(ModTags.Blocks.DUST_ABLE) && !currentBlock.is(ModTags.Blocks.NOT_DUST_ABLE)) {
                            if (random.nextInt(19) == 0) {
                                world.setBlock(currentUpperPos, ModBlocks.DUST.get().defaultBlockState(), 2);
//                                spawnParticle(world, random, currentUpperPos);

                            }

                        }


                    }

                }

            }
        }


        super.randomTick(blockState, world, blockPos, random);

    }

}
