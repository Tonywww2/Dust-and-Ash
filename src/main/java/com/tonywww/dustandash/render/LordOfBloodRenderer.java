package com.tonywww.dustandash.render;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.custom.LordOfBlood;
import com.tonywww.dustandash.models.LordOfBloodModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LordOfBloodRenderer extends GeoItemRenderer<LordOfBlood> {

    public LordOfBloodRenderer() {
        super(new LordOfBloodModel());

    }
}
