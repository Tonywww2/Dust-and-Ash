package com.tonywww.dustandash.registeries;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.entity.CthulhuBossPhase1Entity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuGraphemeEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuLawFieldEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import com.tonywww.dustandash.entity.LightningProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntites {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<EntityType<LightningProjectileEntity>> LIGHTNING_BULLET =
            ENTITY_TYPES.register("lightning_bullet", () -> EntityType.Builder.<LightningProjectileEntity>of(LightningProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("lightning_bullet"));

    public static final RegistryObject<EntityType<CthulhuBossPhase1Entity>> CTHULHU_BOSS_PHASE1 =
            ENTITY_TYPES.register("cthulhu_boss_phase1", () -> EntityType.Builder.<CthulhuBossPhase1Entity>of(CthulhuBossPhase1Entity::new, MobCategory.MONSTER)
                    .sized(3.0f, 3.0f)
                    .fireImmune()
                    .clientTrackingRange(12)
                    .build("cthulhu_boss_phase1"));

    public static final RegistryObject<EntityType<CthulhuPillarEntity>> CTHULHU_PILLAR =
            ENTITY_TYPES.register("cthulhu_pillar", () -> EntityType.Builder.<CthulhuPillarEntity>of(CthulhuPillarEntity::new, MobCategory.MONSTER)
                    .sized(1.2f, 4.0f)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build("cthulhu_pillar"));

    public static final RegistryObject<EntityType<CthulhuStormGolemEntity>> CTHULHU_STORM_GOLEM =
            ENTITY_TYPES.register("cthulhu_storm_golem", () -> EntityType.Builder.<CthulhuStormGolemEntity>of(CthulhuStormGolemEntity::new, MobCategory.MONSTER)
                    .sized(4.0f, 6.0f)
                    .fireImmune()
                    .clientTrackingRange(14)
                    .build("cthulhu_storm_golem"));

    public static final RegistryObject<EntityType<CthulhuGraphemeEntity>> CTHULHU_GRAPHEME =
            ENTITY_TYPES.register("cthulhu_grapheme", () -> EntityType.Builder.<CthulhuGraphemeEntity>of(CthulhuGraphemeEntity::new, MobCategory.MISC)
                    .sized(0.35f, 0.35f)
                    .clientTrackingRange(8)
                    .updateInterval(2)
                    .build("cthulhu_grapheme"));

    public static final RegistryObject<EntityType<CthulhuLawFieldEntity>> CTHULHU_LAW_FIELD =
            ENTITY_TYPES.register("cthulhu_law_field", () -> EntityType.Builder.<CthulhuLawFieldEntity>of(CthulhuLawFieldEntity::new, MobCategory.MONSTER)
                    .sized(2.0f, 0.5f)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build("cthulhu_law_field"));

        public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
