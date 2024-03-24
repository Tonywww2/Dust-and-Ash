package com.tonywww.dustandash.item;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {

    ASH_STEEL("ash_steel", 30, Util.make(new EnumMap<>(ArmorItem.Type.class), (arr) -> {
        arr.put(ArmorItem.Type.BOOTS, 4);
        arr.put(ArmorItem.Type.CHESTPLATE, 9);
        arr.put(ArmorItem.Type.LEGGINGS, 7);
        arr.put(ArmorItem.Type.HELMET, 4);
    }), 18, SoundEvents.ARMOR_EQUIP_IRON, 2.0F, 0.15F, () -> {
        return Ingredient.of(ModItems.ASH_STEEL_INGOT.get());
    }),
    TITANIUM("titanium", 37, Util.make(new EnumMap<>(ArmorItem.Type.class), (arr) -> {
        arr.put(ArmorItem.Type.BOOTS, 4);
        arr.put(ArmorItem.Type.LEGGINGS, 7);
        arr.put(ArmorItem.Type.CHESTPLATE, 7);
        arr.put(ArmorItem.Type.HELMET, 4);
    }), 22, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.25F, 0.25F, () -> {
        return Ingredient.of(ModItems.TITANIUM_INGOT.get());
    })
    ;

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266653_) -> {
        p_266653_.put(ArmorItem.Type.BOOTS, 13);
        p_266653_.put(ArmorItem.Type.LEGGINGS, 15);
        p_266653_.put(ArmorItem.Type.CHESTPLATE, 16);
        p_266653_.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModArmorMaterial(String pName, int pDurabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
        this.name = pName;
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type p_266807_) {
        return HEALTH_FUNCTION_FOR_TYPE.get(p_266807_) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionFunctionForType.get(type);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return DustAndAsh.MOD_ID + ":" + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
