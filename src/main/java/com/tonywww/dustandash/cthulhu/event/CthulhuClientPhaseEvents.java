package com.tonywww.dustandash.cthulhu.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.client.ClientCthulhuPhaseState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT)
public final class CthulhuClientPhaseEvents {

    private CthulhuClientPhaseEvents() {
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (!ClientCthulhuPhaseState.isGridPhase()) {
            return;
        }

        if (!isPhase3Allowed(event.getOverlay().id())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !ClientCthulhuPhaseState.isFightActive()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.getMusicManager().stopPlaying();
        }
    }

    private static boolean isPhase3Allowed(ResourceLocation overlayId) {
        String path = overlayId.getPath();
        if ("hotbar".equals(path) || "chat_panel".equals(path)) {
            return true;
        }
        return path.contains("cthulhu");
    }
}
