package com.tonywww.dustandash.block.custom;

import net.minecraft.world.level.block.Block;

/** Passive attachment: all processing and neighbour checks belong to the centrifuge. */
public class CentrifugeAccelerator extends Block {
    private final int tier;
    private final int durationPercent;

    public CentrifugeAccelerator(Properties properties, int tier, int durationPercent) {
        super(properties);
        this.tier = tier;
        this.durationPercent = durationPercent;
    }

    public int getTier() {
        return tier;
    }

    public int getDurationPercent() {
        return durationPercent;
    }
}
