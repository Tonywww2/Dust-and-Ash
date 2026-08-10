package com.tonywww.dustandash.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicMachineEntity extends SyncedBlockEntity{

    protected int currentTick = 0;
    protected int tickPerOperation = 4;

    public BasicMachineEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    protected final boolean advanceWorkCycle(int ticks) {
        this.currentTick += ticks;
        return this.currentTick >= this.tickPerOperation;
    }

    protected final void resetWorkCycle() {
        this.currentTick = 0;
    }

}
