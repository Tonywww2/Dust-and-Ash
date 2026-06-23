package com.tonywww.dustandash.gecko.models;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.LordOfBlood;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LordOfBloodModel extends GeoModel<LordOfBlood> {
    @Override
    public ResourceLocation getModelResource(LordOfBlood animatable) {
        return DustAndAsh.prefix("geo/item/lord_of_blood.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LordOfBlood animatable) {
        return DustAndAsh.prefix("textures/item/lord_of_blood.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LordOfBlood animatable) {
        return DustAndAsh.prefix("animations/item/lord_of_blood.animation.json");
    }
}
