package com.tonywww.dustandash.client.config;

import com.tonywww.dustandash.DustAndAshConfig;
import net.minecraft.client.Minecraft;

public final class ClientImbaMode {
    private static boolean synchronizedWithServer;
    private static boolean enabled;

    private ClientImbaMode() {
    }

    public static boolean enabled() {
        if (synchronizedWithServer) {
            return enabled;
        }
        return Minecraft.getInstance().getSingleplayerServer() != null
                && DustAndAshConfig.IMBA_MODE.get();
    }

    public static void update(boolean enabled) {
        ClientImbaMode.enabled = enabled;
        synchronizedWithServer = true;
    }

    public static void clear() {
        enabled = false;
        synchronizedWithServer = false;
    }
}