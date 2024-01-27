package com.tonywww.dustandash.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ModConfiguredFeature {

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> BLOODY =
            register("bloody", Feature.TREE.configured((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState()),
                            new SimpleBlockStateProvider(Blocks.GRAVEL.defaultBlockState()),
                            new SpruceFoliagePlacer(FeatureSpread.of(2,1), FeatureSpread.fixed(0), FeatureSpread.of(4, 0)),
                            new StraightTrunkPlacer(4, 1, 1),
                            new TwoLayerFeature(2, 2, 1))).ignoreVines().build())
                    );

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String pId, ConfiguredFeature<FC, ?> pConfiguredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, pId, pConfiguredFeature);
    }

}

