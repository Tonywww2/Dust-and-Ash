package com.tonywww.dustandash.models;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.custom.LordOfBlood;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LordOfBloodModel extends GeoModel<LordOfBlood> {
    @Override
    public ResourceLocation getModelResource(LordOfBlood animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "geo/item/lord_of_blood.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LordOfBlood animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "textures/item/lord_of_blood.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LordOfBlood animatable) {
        return new ResourceLocation(DustAndAsh.MOD_ID, "animations/item/lord_of_blood.animation.json");
    }
}
