package com.tonywww.dustandash.item.FissionReactor;

public class U235FuelLife extends FissionReactorFuelUnit {
    public U235FuelLife(Properties properties) {
        super(properties);
        this.baseHeatRate = 112;
        this.baseNeutronRate = 6;
        this.idealHeat = 640;

    }

}
