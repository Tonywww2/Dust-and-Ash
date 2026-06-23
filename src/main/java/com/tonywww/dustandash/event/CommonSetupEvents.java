package com.tonywww.dustandash.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.entity.CthulhuBossPhase1Entity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuLawFieldEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import com.tonywww.dustandash.network.PacketHandler;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();

        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntites.CTHULHU_BOSS_PHASE1.get(), CthulhuBossPhase1Entity.createAttributes().build());
        event.put(ModEntites.CTHULHU_PILLAR.get(), CthulhuPillarEntity.createAttributes().build());
        event.put(ModEntites.CTHULHU_STORM_GOLEM.get(), CthulhuStormGolemEntity.createAttributes().build());
        event.put(ModEntites.CTHULHU_LAW_FIELD.get(), CthulhuLawFieldEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onConfigLoaded(ModConfigEvent event) {
        if (event.getConfig().getSpec() == DustAndAshConfig.COMMON_CONFIG) {
            CthulhuConfig.syncFromForgeConfig();
        }
    }
}
