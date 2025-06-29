package com.luowenxuan.cementmod.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class AcidRainHandler {
    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        World world = event.world;
        if (world.isRemote || world.getWorldTime() % 200 != 0) return;

        // 在炎热生物群系有10%几率触发酸雨
        if (world.isRaining() && world.rand.nextFloat() < 0.1F) {
            // 修复：添加world参数
            for (Biome biome : world.getBiomeProvider().getBiomesToSpawnIn()) {
                // 获取生物群系温度（需要世界坐标）
                float temperature = biome.getTemperature(new BlockPos(world.getSpawnPoint()));

                if (temperature > 0.5F) {
                    world.getWorldInfo().setRaining(true);
                    world.getWorldInfo().setThundering(true);
                    break;
                }
            }
        }
    }
}
