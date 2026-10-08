package com.tonywww.dustandash.platform;

import net.minecraft.resources.ResourceLocation;
public final class PlatformIdentifier {
    private PlatformIdentifier() {}
    public static ResourceLocation common(String path) {
        //? if forge {
        return id("forge", path);
        //?} else {
        /*return id("c", path);
        *///?}
    }
    public static ResourceLocation id(String namespace, String path) {
//? if forge {
return new ResourceLocation(namespace, path);
//?} else {
/*return ResourceLocation.fromNamespaceAndPath(namespace, path);
*///?}
    }
    public static ResourceLocation parse(String value) {
//? if forge {
return new ResourceLocation(value);
//?} else {
/*return ResourceLocation.parse(value);
*///?}
    }
}
