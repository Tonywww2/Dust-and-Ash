package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.item.FissionReactor.FissionReactorCoolingUnit;
import com.tonywww.dustandash.item.FissionReactor.FissionReactorFuelUnit;

import javax.annotation.Nullable;

public final class ReactorPhysicsEngine {
    private ReactorPhysicsEngine() {
    }

    public static FueledStep calculateFueledStep(ReactorCoreSnapshot core, FissionReactorFuelUnit fuel,
                                                  @Nullable FissionReactorCoolingUnit cooling,
                                                  double currentHeat, int currentNeutron, int ticksPerOperation,
                                                  double minimumEfficiency, double maximumEfficiency,
                                                  double efficiencyMultiplier, double idealHeatRate,
                                                  int maximumHeat) {
        double generatedHeat = calculateGeneratedHeat(core, fuel, cooling);
        double heat = Math.max(0, currentHeat + generatedHeat - Math.sqrt(currentHeat) - 1);
        int efficiency = (int) (Math.max(minimumEfficiency,
                maximumEfficiency - (Math.pow(heat - fuel.getIdealHeat(), 2)
                        / Math.pow(maximumHeat, idealHeatRate)))
                * efficiencyMultiplier * core.fuelCellCount());
        int neutron = currentNeutron
                + (int) (efficiency * fuel.getBaseNeutronRate() * ticksPerOperation);

        return new FueledStep(heat, efficiency, neutron, core.fuelCellCount(), core.coolingCellCount());
    }

    public static double coolDown(double currentHeat, int maximumHeat) {
        return Math.min(Math.max(0, currentHeat - Math.sqrt(currentHeat) - 1), maximumHeat);
    }

    public static EnergyStep convertExcessNeutrons(int currentNeutron, int currentEnergy,
                                                    int maximumNeutron, int maximumEnergy,
                                                    double neutronToEnergyRatio) {
        if (currentNeutron <= maximumNeutron / 2) {
            return new EnergyStep(currentNeutron, currentEnergy, 0);
        }

        int usedNeutron = currentNeutron - (maximumNeutron / 2);
        int generatedEnergy = (int) (usedNeutron * neutronToEnergyRatio);
        int energy = Math.min(maximumEnergy, currentEnergy + generatedEnergy);
        return new EnergyStep(currentNeutron - usedNeutron, energy, generatedEnergy);
    }

    private static double calculateGeneratedHeat(ReactorCoreSnapshot core, FissionReactorFuelUnit fuel,
                                                  @Nullable FissionReactorCoolingUnit cooling) {
        double totalHeat = 0;
        double coolingRate = cooling == null ? 1 : cooling.getBaseCoolingRate();
        for (ReactorCoreSnapshot.FuelCellEnvironment cell : core.fuelCells()) {
            double cellHeat = fuel.getBaseHeatRate()
                    * Math.sqrt((cell.surroundingFuelCells() + 1)
                    / ((cell.surroundingCoolingCells() + 1) * coolingRate));
            totalHeat += cellHeat;
        }
        return totalHeat;
    }

    public record FueledStep(double heat, int efficiency, int neutron,
                             int fuelCellCount, int coolingCellCount) {
    }

    public record EnergyStep(int neutron, int energy, int generatedEnergy) {
    }
}