package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public record ReactorStructureSnapshot(int radius, int height, @Nullable BlockPos interfacePos) {
}