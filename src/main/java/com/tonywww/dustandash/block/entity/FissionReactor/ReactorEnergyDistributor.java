package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.Queue;

public final class ReactorEnergyDistributor {
    private ReactorEnergyDistributor() {
    }

    public static int distribute(Level level, BlockPos origin, Queue<Direction> directions,
                                 int storedEnergy, int maximumTransfer) {
        if (storedEnergy <= 0 || directions.isEmpty()) {
            return storedEnergy;
        }

        directions.offer(directions.remove());
        int remainingEnergy = storedEnergy;
        for (Direction direction : directions) {
            if (remainingEnergy <= 0) {
                break;
            }

            BlockEntity neighbor = level.getBlockEntity(origin.relative(direction));
            if (neighbor == null) {
                continue;
            }

            int offer = Math.min(maximumTransfer, remainingEnergy);
            int[] transferred = {0};
            neighbor.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                if (storage.canReceive()) {
                    transferred[0] = storage.receiveEnergy(offer, false);
                }
            });
            remainingEnergy -= transferred[0];
        }
        return remainingEnergy;
    }
}