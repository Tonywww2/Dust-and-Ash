package com.tonywww.dustandash.gecko.models;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.GaleOtaijutsu;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GaleOtaijutsuModel extends GeoModel<GaleOtaijutsu> {
    @Override
    public ResourceLocation getModelResource(GaleOtaijutsu animatable) {
        return DustAndAsh.prefix("geo/item/gale_otaijutsu.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GaleOtaijutsu animatable) {
        return DustAndAsh.prefix("textures/item/gale_otaijutsu.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GaleOtaijutsu animatable) {
        return DustAndAsh.prefix("animations/item/gale_otaijutsu.animation.json");
    }
}
