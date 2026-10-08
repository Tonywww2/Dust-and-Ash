package com.tonywww.dustandash.platform;

import net.minecraft.world.item.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import com.tonywww.dustandash.registry.DAAArmorMaterial;
public final class PlatformEquipment {
    private PlatformEquipment() {}
//? if forge {
public static void register(PlatformEventBus bus) {}
public static SoundEvent ironArmorSound() { return SoundEvents.ARMOR_EQUIP_IRON; }
public static SoundEvent netheriteArmorSound() { return SoundEvents.ARMOR_EQUIP_NETHERITE; }
public static Item armor(PlatformArmorMaterial material, ArmorItem.Type type, Item.Properties properties) { return new ArmorItem(material,type,properties); }
//?} else {
/*private static final net.neoforged.neoforge.registries.DeferredRegister<ArmorMaterial> MATERIALS=net.neoforged.neoforge.registries.DeferredRegister.create(net.minecraft.core.registries.Registries.ARMOR_MATERIAL,"dustandash");
private static final net.neoforged.neoforge.registries.DeferredHolder<ArmorMaterial,ArmorMaterial> ASH_STEEL=MATERIALS.register("ash_steel",()->material(DAAArmorMaterial.ASH_STEEL));
private static final net.neoforged.neoforge.registries.DeferredHolder<ArmorMaterial,ArmorMaterial> TITANIUM=MATERIALS.register("titanium",()->material(DAAArmorMaterial.TITANIUM));
public static void register(PlatformEventBus bus) { MATERIALS.register(bus.nativeBus()); }
public static SoundEvent ironArmorSound() { return SoundEvents.ARMOR_EQUIP_IRON.value(); }
public static SoundEvent netheriteArmorSound() { return SoundEvents.ARMOR_EQUIP_NETHERITE.value(); }
private static ArmorMaterial material(PlatformArmorMaterial material) {
    var defense=new java.util.EnumMap<ArmorItem.Type,Integer>(ArmorItem.Type.class);
    for (var type:ArmorItem.Type.values()) defense.put(type,material.getDefenseForType(type));
    return new ArmorMaterial(defense,material.getEnchantmentValue(),net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.wrapAsHolder(material.getEquipSound()),material::getRepairIngredient,java.util.List.of(new ArmorMaterial.Layer(PlatformIdentifier.parse(material.getName()))),material.getToughness(),material.getKnockbackResistance());
}
public static Item armor(PlatformArmorMaterial material, ArmorItem.Type type, Item.Properties properties) {
    return new ArmorItem(material==DAAArmorMaterial.ASH_STEEL ? ASH_STEEL : TITANIUM,type,properties.durability(material.getDurabilityForType(type)));
}
*///?}

    public static Item sword(Tier tier, int damage, float speed, Item.Properties properties) { return new PlatformSwordItem(tier,damage,speed,properties); }
public static Item pickaxe(Tier tier, int damage, float speed, Item.Properties properties) {
//? if forge {
return new PickaxeItem(tier,damage,speed,properties);
//?} else {
/*return new PickaxeItem(tier,properties.attributes(DiggerItem.createAttributes(tier,damage,speed)));
*///?}
}
public static Item shovel(Tier tier, float damage, float speed, Item.Properties properties) {
//? if forge {
return new ShovelItem(tier,damage,speed,properties);
//?} else {
/*return new ShovelItem(tier,properties.attributes(DiggerItem.createAttributes(tier,damage,speed)));
*///?}
}
public static Item axe(Tier tier, float damage, float speed, Item.Properties properties) {
//? if forge {
return new AxeItem(tier,damage,speed,properties);
//?} else {
/*return new AxeItem(tier,properties.attributes(DiggerItem.createAttributes(tier,damage,speed)));
*///?}
}
public static Item hoe(Tier tier, int damage, float speed, Item.Properties properties) {
//? if forge {
return new HoeItem(tier,damage,speed,properties);
//?} else {
/*return new HoeItem(tier,properties.attributes(DiggerItem.createAttributes(tier,damage,speed)));
*///?}
}}
