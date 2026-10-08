package com.tonywww.dustandash.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
public final class PlatformGeometry {
    private PlatformGeometry() {}
    public static AABB box(BlockPos first, BlockPos second) {
        return new AABB(first.getX(), first.getY(), first.getZ(), second.getX(), second.getY(), second.getZ());
    }
}
