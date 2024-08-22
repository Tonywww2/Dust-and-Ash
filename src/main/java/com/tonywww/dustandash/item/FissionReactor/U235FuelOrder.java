package com.tonywww.dustandash.item.FissionReactor;

public class U235FuelOrder extends FissionReactorFuelUnit {
    public U235FuelOrder(Properties properties) {
        super(properties);
        this.baseHeatRate = 84;
        this.baseNeutronRate = 6;
        this.idealHeat = 640;

    }

}
