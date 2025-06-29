package com.luowenxuan.cementmod.integration;

import com.luowenxuan.cementmod.CementMod;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration {
    public static boolean isJEILoaded = false;
    public static boolean isTOPLoaded = false;

    public static void checkLoadedMods() {
        isJEILoaded = Loader.isModLoaded("jei");
        isTOPLoaded = Loader.isModLoaded("theoneprobe");
    }

    public static void integrate() {
        if (isJEILoaded) {
            // JEI集成
            CementMod.logger.info("JEI detected, adding recipe categories");
        }

        if (isTOPLoaded) {
            // The One Probe集成
            CementMod.logger.info("The One Probe detected, adding machine info");
        }
    }
}