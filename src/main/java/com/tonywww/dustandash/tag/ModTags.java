package com.tonywww.dustandash.tag;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> DUST_ABLE = createTag("dust_able");
        public static final TagKey<Block> NOT_DUST_ABLE = createTag("not_dust_able");
        public static final TagKey<Block> FISSION_REACTOR_WALL = createTag("fission_reactor_wall");

        private static TagKey<Block> createTag(String name) {

            return BlockTags.create(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static TagKey<Block> createForgeTag(String name) {

            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }

    public static class Items {

        public static final TagKey<Item> CRAFT_MATERIAL = createTag("craft_material");
        public static final TagKey<Item> MILLING_BLACKLIST = createTag("milling_blacklist");
        public static final TagKey<Item> CENTRIFUGE_CATALYST = createTag("centrifuge_catalyst");
        public static final TagKey<Item> NANO_BLACKLIST = createTag("nano_blacklist");
        public static final TagKey<Item> NEUTRON_CONTAINER = createTag("neutron_container");

        public static final TagKey<Item> ASH = createForgeTag("ash");

        private static TagKey<Item> createTag(String name) {

            return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static TagKey<Item> createForgeTag(String name) {

            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

    public static class DamageTypes {
        public static final TagKey<DamageType> LIGHT_HALO_EXCLUDED = createTag("light_halo_excluded");
        public static final TagKey<DamageType> DARK_HALO_EXCLUDED = createTag("dark_halo_excluded");

        private static TagKey<DamageType> createTag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(DustAndAsh.MOD_ID, name));
        }
    }

    public static class MobEffects {
        public static final TagKey<MobEffect> DARK_HALO_EFFECTS = createTag("dark_halo_effects");

        private static TagKey<MobEffect> createTag(String name) {
            return TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(DustAndAsh.MOD_ID, name));
        }
    }

}
