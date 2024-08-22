package com.tonywww.dustandash.item.FissionReactor;

public class U235FuelFire extends FissionReactorFuelUnit {
    public U235FuelFire(Properties properties) {
        super(properties);
        this.baseHeatRate = 168;
        this.baseNeutronRate = 7.5;
        this.idealHeat = 960;

    }

}
