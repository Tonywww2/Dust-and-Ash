package com.tonywww.dustandash.item.FissionReactor;

public class BasicFuel extends FissionReactorFuelUnit {
    public BasicFuel(Properties properties) {
        super(properties);
        this.baseHeatRate = 112;
        this.baseNeutronRate = 6;
        this.idealHeat = 640;

    }

}
