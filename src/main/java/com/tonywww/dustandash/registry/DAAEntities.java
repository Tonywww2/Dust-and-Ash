package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.entity.LightStaffEntity;
import com.tonywww.dustandash.entity.LightningProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DAAEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DustAndAsh.MOD_ID);

    public static final RegistryObject<EntityType<LightningProjectileEntity>> LIGHTNING_BULLET =
            ENTITY_TYPES.register("lightning_bullet", () -> EntityType.Builder.<LightningProjectileEntity>of(LightningProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("lightning_bullet"));

    public static final RegistryObject<EntityType<LightStaffEntity>> LIGHT_STAFF =
            ENTITY_TYPES.register("light_staff", () -> EntityType.Builder
                    .<LightStaffEntity>of(LightStaffEntity::new, MobCategory.MISC)
                    .sized(0.5f, 5f)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build("light_staff"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
