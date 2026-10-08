package com.tonywww.dustandash.platform;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
public final class PlatformRegistries {
    public static final RegistryHandle<Item> ITEMS = new RegistryHandle<>(Registries.ITEM, BuiltInRegistries.ITEM);
    public static final RegistryHandle<Block> BLOCKS = new RegistryHandle<>(Registries.BLOCK, BuiltInRegistries.BLOCK);
    public static final RegistryHandle<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new RegistryHandle<>(Registries.BLOCK_ENTITY_TYPE, BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final RegistryHandle<EntityType<?>> ENTITY_TYPES = new RegistryHandle<>(Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE);
    public static final RegistryHandle<MenuType<?>> MENU_TYPES = new RegistryHandle<>(Registries.MENU, BuiltInRegistries.MENU);
    public static final RegistryHandle<ParticleType<?>> PARTICLE_TYPES = new RegistryHandle<>(Registries.PARTICLE_TYPE, BuiltInRegistries.PARTICLE_TYPE);
    public static final RegistryHandle<RecipeSerializer<?>> RECIPE_SERIALIZERS = new RegistryHandle<>(Registries.RECIPE_SERIALIZER, BuiltInRegistries.RECIPE_SERIALIZER);
    public static final RegistryHandle<RecipeType<?>> RECIPE_TYPES = new RegistryHandle<>(Registries.RECIPE_TYPE, BuiltInRegistries.RECIPE_TYPE);
    private PlatformRegistries() {}
    public record RegistryHandle<T>(ResourceKey<? extends Registry<T>> key, Registry<T> registry) {
        public ResourceLocation getKey(T value) { return registry.getKey(value); }
        public T getValue(ResourceLocation id) { return registry.containsKey(id) ? registry.get(id) : null; }
    }
}
