package com.luowenxuan.cementmod;

import com.luowenxuan.cementmod.events.BlockEventHandler;
import com.luowenxuan.cementmod.fluids.ModFluids;
import com.luowenxuan.cementmod.gui.ContainerCrusher;
import com.luowenxuan.cementmod.gui.ContainerRotaryKiln;
import com.luowenxuan.cementmod.gui.GuiCrusher;
import com.luowenxuan.cementmod.gui.GuiRotaryKiln;
import com.luowenxuan.cementmod.integration.ModIntegration;
import com.luowenxuan.cementmod.tiles.TileCrusher;
import com.luowenxuan.cementmod.tiles.TileRotaryKiln;
import com.luowenxuan.cementmod.world.AcidRainHandler;
import com.luowenxuan.cementmod.world.ModWorldGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

@Mod(modid = CementMod.MODID, name = CementMod.NAME, version = CementMod.VERSION)
public class CementMod {
    public static final String MODID = "cementmod";
    public static final String NAME = "Cement Mod";
    public static final String VERSION = "1.0";
    public static CementMod instance;

    // 日志记录器
    public static final Logger logger = LogManager.getLogger(MODID);

    // 酸雨配置（使用集合提高效率）
    private static boolean acidRainEnabled = true;
    private static List<Integer> acidRainDimensions = Arrays.asList(0); // 默认主世界

    public CementMod() {
        instance = this;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Starting Cement Mod pre-initialization");

        // 注册流体
        ModFluids.register();

        // 注册世界生成
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);

        // 注册GUI处理器
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        // 注册方块事件处理器
        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());

        // 注册TileEntity
        GameRegistry.registerTileEntity(TileCrusher.class, new ResourceLocation(MODID, "crusher"));
        GameRegistry.registerTileEntity(TileRotaryKiln.class, new ResourceLocation(MODID, "rotary_kiln"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Starting Cement Mod initialization");

        // 注册酸雨事件处理器（确保在init阶段注册）
        MinecraftForge.EVENT_BUS.register(new AcidRainHandler());

        // 检查其他Mod集成
        ModIntegration.checkLoadedMods();
        ModIntegration.integrate();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Cement Mod initialization complete");
    }

    // GUI处理器类
    public static class GuiHandler implements IGuiHandler {
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);

            if (te == null) return null;

            switch (ID) {
                case 0: // 破碎机
                    if (te instanceof TileCrusher) {
                        return new ContainerCrusher(player.inventory, (TileCrusher) te);
                    }
                    break;
                case 1: // 回转窑
                    if (te instanceof TileRotaryKiln) {
                        return new ContainerRotaryKiln(player.inventory, (TileRotaryKiln) te);
                    }
                    break;
            }
            return null;
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity te = world.getTileEntity(pos);

            if (te == null) return null;

            switch (ID) {
                case 0: // 破碎机GUI
                    if (te instanceof TileCrusher) {
                        return new GuiCrusher((TileCrusher) te, new ContainerCrusher(player.inventory, (TileCrusher) te));
                    }
                    break;
                case 1: // 回转窑GUI
                    if (te instanceof TileRotaryKiln) {
                        return new GuiRotaryKiln((TileRotaryKiln) te, new ContainerRotaryKiln(player.inventory, (TileRotaryKiln) te));
                    }
                    break;
            }
            return null;
        }
    }

    // ========== 酸雨配置方法 ==========
    public static boolean isAcidRainEnabled() {
        return acidRainEnabled;
    }

    public static void setAcidRainEnabled(boolean enabled) {
        acidRainEnabled = enabled;
    }

    // 使用维度管理器检查维度有效性
    public static boolean isAcidRainInDimension(int dimensionId) {
        // 检查维度是否有效存在
        if (DimensionManager.getWorld(dimensionId) == null) {
            logger.warn("Invalid dimension ID detected: " + dimensionId);
            return false;
        }

        return acidRainDimensions.contains(dimensionId);
    }

    // 添加维度到酸雨列表
    public static void addAcidRainDimension(int dimensionId) {
        if (!acidRainDimensions.contains(dimensionId)) {
            acidRainDimensions.add(dimensionId);
            logger.info("Added dimension " + dimensionId + " to acid rain list");
        }
    }

    // 从酸雨列表移除维度
    public static void removeAcidRainDimension(int dimensionId) {
        if (acidRainDimensions.contains(dimensionId)) {
            acidRainDimensions.remove(Integer.valueOf(dimensionId));
            logger.info("Removed dimension " + dimensionId + " from acid rain list");
        }
    }
}
