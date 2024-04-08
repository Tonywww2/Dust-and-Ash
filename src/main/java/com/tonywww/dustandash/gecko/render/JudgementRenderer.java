package com.tonywww.dustandash.gecko.render;

import com.tonywww.dustandash.item.Judgement;
import com.tonywww.dustandash.gecko.models.JudgementModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class JudgementRenderer extends GeoItemRenderer<Judgement> {

    public JudgementRenderer() {
        super(new JudgementModel());

    }
}
