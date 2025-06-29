package com.luowenxuan.cementmod.fluids;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.block.BlockFluidWetConcrete;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
    public static Fluid WET_CONCRETE;
    public static BlockFluidWetConcrete WET_CONCRETE_BLOCK;

    public static void register() {
        // 创建流体实例
        WET_CONCRETE = new Fluid(
                "wet_concrete",
                new ResourceLocation(CementMod.MODID, "blocks/wet_concrete_still"),
                new ResourceLocation(CementMod.MODID, "blocks/wet_concrete_flow")
        ).setViscosity(3000)  // 高粘度 = 流动慢
                .setDensity(2000)    // 高密度 = 沉底
                .setTemperature(300) // 室温
                .setLuminosity(0);   // 无发光

        // 注册流体
        FluidRegistry.registerFluid(WET_CONCRETE);

        // 确保流体可被桶放置
        FluidRegistry.addBucketForFluid(WET_CONCRETE);

        // 创建流体方块
        WET_CONCRETE_BLOCK = new BlockFluidWetConcrete();

        // 关联流体和方块
        WET_CONCRETE.setBlock(WET_CONCRETE_BLOCK);
    }
}