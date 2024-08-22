package com.tonywww.dustandash.item.FissionReactor;

public class U235Fuel extends FissionReactorFuelUnit {
    public U235Fuel(Properties properties) {
        super(properties);
        this.baseHeatRate = 112;
        this.baseNeutronRate = 6;
        this.idealHeat = 640;

    }

}
