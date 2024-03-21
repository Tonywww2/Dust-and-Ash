package com.tonywww.dustandash.render;

import com.tonywww.dustandash.item.custom.Judgement;
import com.tonywww.dustandash.models.JudgementModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class JudgementRenderer extends GeoItemRenderer<Judgement> {

    public JudgementRenderer() {
        super(new JudgementModel());

    }
}
