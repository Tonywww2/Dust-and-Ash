package com.tonywww.dustandash.platform;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
//? if forge {
public interface PlatformArmorMaterial extends net.minecraft.world.item.ArmorMaterial {
//?} else {
/*public interface PlatformArmorMaterial {
*///?}

    int getDurabilityForType(ArmorItem.Type type);
    int getDefenseForType(ArmorItem.Type type);
    int getEnchantmentValue();
    SoundEvent getEquipSound();
    Ingredient getRepairIngredient();
    String getName();
    float getToughness();
    float getKnockbackResistance();
}
