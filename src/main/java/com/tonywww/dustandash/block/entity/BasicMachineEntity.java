package com.tonywww.dustandash.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

class BasicMachineEntity extends SyncedBlockEntity{

    protected int currentTick = 0;
    protected int tickPerOperation = 4;

    public BasicMachineEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    /**
     * Should be only use on server side
     *
     * @param be
     * @param tick
     */
    public static void tick(BasicMachineEntity be, int tick) {
        be.currentTick += tick;

    }

    /**
     * Should be only use on server side
     *
     * @param be
     * @return
     */
    public static boolean isWorkingTick(BasicMachineEntity be) {
        return be.currentTick >= be.tickPerOperation;
    }

    /**
     * Should be only use on server side, in the end of the working tick
     *
     * @param be
     */
    public static void resetTicker(BasicMachineEntity be) {
        be.currentTick = 0;

    }

}
