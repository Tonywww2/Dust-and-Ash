package com.tonywww.dustandash.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import java.util.function.Supplier;

public enum ModItemTier implements Tier {
    FLINT(0, 48, 3.0F, 1.0F, 20, () -> {
        return Ingredient.of(ModItems.BLOODY_FLINT.get());
    }),
    ASH_STEEL(3, 880, 7.5F, 2.5F, 16, () -> {
        return Ingredient.of(ModItems.ASH_STEEL_INGOT.get());
    }),
    TITANIUM_ALLOY(5, 3280, 12.5F, 6.0F, 28, () -> {
        return Ingredient.of(ModItems.TITANIUM_INGOT.get());
    })
    ;

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModItemTier(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
