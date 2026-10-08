package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.item.*;
import net.minecraft.world.item.*;
import com.tonywww.dustandash.platform.RegistryObject;

public final class DAAEquipmentItems {
    public static final RegistryObject<Item> HAND_VACUUM = DAAItems.register("hand_vacuum",
            () -> new HandVacuum(new Item.Properties().stacksTo(1).durability(38)));
    public static final RegistryObject<Item> IRON_VACUUM = DAAItems.register("iron_vacuum",
            () -> new IronVacuum(new Item.Properties().stacksTo(1).durability(540)));
    public static final RegistryObject<Item> SHARPEN_FLINT = DAAItems.register("sharpen_flint",
            () -> new SharpenFlint(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> FLINT_PICKAXE = DAAItems.register("flint_pickaxe",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.pickaxe(DAAItemTier.FLINT, 4, -2.4F, new Item.Properties()));

    public static final RegistryObject<Item> ASH_STEEL_BOOTS = DAAItems.register("ash_steel_boots",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.armor(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_CHESTPLATE = DAAItems.register("ash_steel_chestplate",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.armor(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_LEGGING = DAAItems.register("ash_steel_leggings",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.armor(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_HELMET = DAAItems.register("ash_steel_helmet",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.armor(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ASH_STEEL_SWORD = DAAItems.register("ash_steel_sword",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.sword(DAAItemTier.ASH_STEEL, 3, -2.3F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_SHOVEL = DAAItems.register("ash_steel_shovel",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.shovel(DAAItemTier.ASH_STEEL, 1.5F, -2.9F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_PICKAXE = DAAItems.register("ash_steel_pickaxe",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.pickaxe(DAAItemTier.ASH_STEEL, 1, -2.7F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_AXE = DAAItems.register("ash_steel_axe",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.axe(DAAItemTier.ASH_STEEL, 5.5F, -3.05F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_HOE = DAAItems.register("ash_steel_hoe",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.hoe(DAAItemTier.ASH_STEEL, -2, -0.9F, new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_ALLOY_SWORD = DAAItems.register("titanium_alloy_sword",
            () -> new TitaniumAlloySword(DAAItemTier.TITANIUM_ALLOY, 2, -2.0F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TITANIUM_ALLOY_GREAT_SWORD = DAAItems.register("titanium_alloy_great_sword",
            () -> new TitaniumAlloyGreatSword(DAAItemTier.TITANIUM_ALLOY, 7, -2.9F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TITANIUM_ALLOY_PICKAXE = DAAItems.register("titanium_alloy_pickaxe",
            () -> com.tonywww.dustandash.platform.PlatformEquipment.pickaxe(DAAItemTier.TITANIUM_ALLOY, 1, -2.7F,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> GALE_OTAIJUTSU = DAAItems.register("gale_otaijutsu",
            () -> new GaleOtaijutsu(DAAItemTier.TITANIUM_ALLOY, 2, -1.2F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SUNBURN_MEGA_SWORD = DAAItems.register("sunburn_mega_sword",
            () -> new SunburnMegaSword(DAAItemTier.TITANIUM_ALLOY, 10, -3.1F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> WHITE_LIGHTNING = DAAItems.register("white_lightning",
            () -> new WhiteLightning(DAAItemTier.TITANIUM_ALLOY, 3, -2.0F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> LORD_OF_BLOOD = DAAItems.register("lord_of_blood",
            () -> new LordOfBlood(DAAItemTier.TITANIUM_ALLOY, 4, -2.6F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> ROTTEN_BLADE = DAAItems.register("rotten_blade",
            () -> new RottenBlade(DAAItemTier.TITANIUM_ALLOY, 5, -2.0F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> JUDGEMENT = DAAItems.register("judgement",
            () -> new Judgement(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> LIGHT_FORGED_HALO = DAAItems.register("light_forged_halo",
            () -> new LightForgedHalo(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> DARK_FORGED_HALO = DAAItems.register("dark_forged_halo",
            () -> new DarkForgedHalo(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> VOID_RING = DAAItems.register("void_ring",
            () -> new VoidRing(new Item.Properties().stacksTo(1).fireResistant()));

    private DAAEquipmentItems() {
    }

    static void bootstrap() {
    }
}
