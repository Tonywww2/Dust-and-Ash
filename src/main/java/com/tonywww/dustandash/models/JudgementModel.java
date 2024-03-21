package com.tonywww.dustandash.models;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.custom.Judgement;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class JudgementModel extends GeoModel<Judgement> {
    @Override
    public ResourceLocation getModelResource(Judgement animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "geo/item/judgement.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Judgement animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "textures/item/judgement.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Judgement animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "animations/item/judgement.animation.json");
    }
}
