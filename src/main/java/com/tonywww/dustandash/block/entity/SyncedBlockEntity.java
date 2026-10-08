package com.tonywww.dustandash.block.entity;
import com.tonywww.dustandash.platform.PlatformBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
public class SyncedBlockEntity extends PlatformBlockEntity {
    public SyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }
}
