package com.tonywww.dustandash.item.FissionReactor;

public class U235FuelMetal extends FissionReactorFuelUnit {
    public U235FuelMetal(Properties properties) {
        super(properties);
        this.baseHeatRate = 112;
        this.baseNeutronRate = 9;
        this.idealHeat = 640;

    }

}
