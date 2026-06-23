package com.tonywww.dustandash.gecko.models;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.Judgement;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class JudgementModel extends GeoModel<Judgement> {
    @Override
    public ResourceLocation getModelResource(Judgement animatable) {
        return DustAndAsh.prefix("geo/item/judgement.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Judgement animatable) {
        return DustAndAsh.prefix("textures/item/judgement.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Judgement animatable) {
        return DustAndAsh.prefix("animations/item/judgement.animation.json");
    }
}
