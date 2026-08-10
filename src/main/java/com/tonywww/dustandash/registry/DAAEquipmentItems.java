package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.item.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;

public final class DAAEquipmentItems {
    public static final RegistryObject<Item> HAND_VACUUM = DAAItems.register("hand_vacuum",
            () -> new HandVacuum(new Item.Properties().stacksTo(1).durability(38)));
    public static final RegistryObject<Item> IRON_VACUUM = DAAItems.register("iron_vacuum",
            () -> new IronVacuum(new Item.Properties().stacksTo(1).durability(540)));
    public static final RegistryObject<Item> SHARPEN_FLINT = DAAItems.register("sharpen_flint",
            () -> new SharpenFlint(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> FLINT_PICKAXE = DAAItems.register("flint_pickaxe",
            () -> new PickaxeItem(DAAItemTier.FLINT, 4, -2.4F, new Item.Properties()));

    public static final RegistryObject<Item> ASH_STEEL_BOOTS = DAAItems.register("ash_steel_boots",
            () -> new ArmorItem(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_CHESTPLATE = DAAItems.register("ash_steel_chestplate",
            () -> new ArmorItem(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_LEGGING = DAAItems.register("ash_steel_leggings",
            () -> new ArmorItem(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_HELMET = DAAItems.register("ash_steel_helmet",
            () -> new ArmorItem(DAAArmorMaterial.ASH_STEEL, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ASH_STEEL_SWORD = DAAItems.register("ash_steel_sword",
            () -> new SwordItem(DAAItemTier.ASH_STEEL, 3, -2.3F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_SHOVEL = DAAItems.register("ash_steel_shovel",
            () -> new ShovelItem(DAAItemTier.ASH_STEEL, 1.5F, -2.9F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_PICKAXE = DAAItems.register("ash_steel_pickaxe",
            () -> new PickaxeItem(DAAItemTier.ASH_STEEL, 1, -2.7F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_AXE = DAAItems.register("ash_steel_axe",
            () -> new AxeItem(DAAItemTier.ASH_STEEL, 5.5F, -3.05F, new Item.Properties()));
    public static final RegistryObject<Item> ASH_STEEL_HOE = DAAItems.register("ash_steel_hoe",
            () -> new HoeItem(DAAItemTier.ASH_STEEL, -2, -0.9F, new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_ALLOY_SWORD = DAAItems.register("titanium_alloy_sword",
            () -> new TitaniumAlloySword(DAAItemTier.TITANIUM_ALLOY, 2, -2.0F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TITANIUM_ALLOY_GREAT_SWORD = DAAItems.register("titanium_alloy_great_sword",
            () -> new TitaniumAlloyGreatSword(DAAItemTier.TITANIUM_ALLOY, 7, -2.9F,
                    new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> TITANIUM_ALLOY_PICKAXE = DAAItems.register("titanium_alloy_pickaxe",
            () -> new PickaxeItem(DAAItemTier.TITANIUM_ALLOY, 1, -2.7F,
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
            () -> new Judgement(DAAItemTier.TITANIUM_ALLOY, 17, -3.2F,
                    new Item.Properties().fireResistant()));

    private DAAEquipmentItems() {
    }

    static void bootstrap() {
    }
}