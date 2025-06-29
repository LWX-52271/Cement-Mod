package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.fluids.ModFluids;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ItemRegistryHandler
{
    public static final ItemLimePowder LIME_POWDER = new ItemLimePowder();
    public static final ItemCementPowder CEMENT_POWDER = new ItemCementPowder();
    public static final ItemIronPlate IRON_PLATE = new ItemIronPlate();
    public static final ItemLargeCrushingWheel LARGE_CRUSHING_WHEEL = new ItemLargeCrushingWheel();
    public static final ItemSmallCrushingWheel SMALL_CRUSHING_WHEEL = new ItemSmallCrushingWheel();
    public static final ItemCrushingAxle CRUSHING_AXLE = new ItemCrushingAxle();
    public static final ItemCompleteCrushingAxle COMPLETE_CRUSHING_AXLE = new ItemCompleteCrushingAxle();
    public static final ItemRawMixture RAW_MIXTURE = new ItemRawMixture();
    public static final ItemRefractoryBrick REFRACTORY_BRICK = new ItemRefractoryBrick();
    public static final ItemHammer HAMMER = new ItemHammer();
    public static final ItemWetConcreteBucket WET_CONCRETE_BUCKET = new ItemWetConcreteBucket();
    public static final ItemBlock ITEM_WET_CONCRETE = withRegistryName(new ItemBlock(ModFluids.WET_CONCRETE_BLOCK));
    public static final ItemBlock ITEM_DEEP_CLAY = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_DEEP_CLAY));
    public static final ItemBlock ITEM_LIMESTONE = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_LIMESTONE));
    public static final ItemBlock ITEM_HARDENED_CONCRETE = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_HARDENED_CONCRETE));
    public static final ItemBlock ITEM_STEEL_FRAME = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_STEEL_FRAME));
    public static final ItemBlock ITEM_BLOCK_REFRACTORY_BRICK_BLOCK = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_REFRACTORY_BRICK_BLOCK));
    public static final ItemBlock ITEM_REINFORCED_CONCRETE = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_REINFORCED_CONCRETE));
    public static final ItemBlock ITEM_STEEL_REBAR = withRegistryName(new ItemBlock(BlockRegistryHandler.BLOCK_STEEL_REBAR));
    public static final ItemBlock ITEM_CRUSHER = withRegistryName(
            new ItemBlockMachine(
                    BlockRegistryHandler.BLOCK_CRUSHER,
                    "tooltip.cementmod.crusher_structure"
            )
    );
    public static final ItemBlock ITEM_ROTARY_KILN = withRegistryName(
            new ItemBlockMachine(
                    BlockRegistryHandler.BLOCK_ROTARY_KILN,
                    "tooltip.cementmod.rotary_kiln_structure"
            )
    );

    private static ItemBlock withRegistryName(ItemBlock item)
    {
        item.setRegistryName(item.getBlock().getRegistryName());
        return item;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(LIME_POWDER);
        registry.register(CEMENT_POWDER);
        registry.register(IRON_PLATE);
        registry.register(LARGE_CRUSHING_WHEEL);
        registry.register(SMALL_CRUSHING_WHEEL);
        registry.register(CRUSHING_AXLE);
        registry.register(COMPLETE_CRUSHING_AXLE);
        registry.register(RAW_MIXTURE);
        registry.register(REFRACTORY_BRICK);
        registry.register(HAMMER);
        registry.register(ITEM_DEEP_CLAY);
        registry.register(ITEM_LIMESTONE);
        registry.register(ITEM_HARDENED_CONCRETE);
        registry.register(ITEM_STEEL_FRAME);
        registry.register(ITEM_CRUSHER);
        registry.register(ITEM_BLOCK_REFRACTORY_BRICK_BLOCK);
        registry.register(ITEM_ROTARY_KILN);
        registry.register(WET_CONCRETE_BUCKET);
        registry.register(ITEM_WET_CONCRETE);
        registry.register(ITEM_REINFORCED_CONCRETE);
        registry.register(ITEM_STEEL_REBAR);
        // 添加矿石词典
        OreDictionary.registerOre("toolHammer", HAMMER);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item)
    {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(),"inventory");
        ModelLoader.setCustomModelResourceLocation(item,0,modelResourceLocation);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event)
    {
        registerModel(LIME_POWDER);
        registerModel(CEMENT_POWDER);
        registerModel(IRON_PLATE);
        registerModel(LARGE_CRUSHING_WHEEL);
        registerModel(SMALL_CRUSHING_WHEEL);
        registerModel(CRUSHING_AXLE);
        registerModel(COMPLETE_CRUSHING_AXLE);
        registerModel(RAW_MIXTURE);
        registerModel(REFRACTORY_BRICK);
        registerModel(HAMMER);
        registerModel(ITEM_DEEP_CLAY);
        registerModel(ITEM_LIMESTONE);
        registerModel(ITEM_HARDENED_CONCRETE);
        registerModel(ITEM_STEEL_FRAME);
        registerModel(ITEM_CRUSHER);
        registerModel(ITEM_BLOCK_REFRACTORY_BRICK_BLOCK);
        registerModel(ITEM_ROTARY_KILN);
        registerModel(WET_CONCRETE_BUCKET);
        registerModel(ITEM_WET_CONCRETE);
        registerModel(ITEM_REINFORCED_CONCRETE);
        registerModel(ITEM_STEEL_REBAR);
    }
}
