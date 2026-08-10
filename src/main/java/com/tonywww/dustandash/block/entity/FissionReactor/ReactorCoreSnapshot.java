package com.tonywww.dustandash.block.entity.FissionReactor;

import java.util.List;

public record ReactorCoreSnapshot(List<FuelCellEnvironment> fuelCells, int coolingCellCount) {
    public ReactorCoreSnapshot {
        fuelCells = List.copyOf(fuelCells);
    }

    public int fuelCellCount() {
        return this.fuelCells.size();
    }

    public record FuelCellEnvironment(int surroundingFuelCells, int surroundingCoolingCells) {
    }
}