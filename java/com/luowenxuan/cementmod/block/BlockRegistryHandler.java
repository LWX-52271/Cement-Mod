package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.fluids.ModFluids;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class BlockRegistryHandler
{
    public static final BlockDeepClay BLOCK_DEEP_CLAY = new BlockDeepClay();
    public static final BlockLimestone BLOCK_LIMESTONE = new BlockLimestone();
    public static final BlockHardenedConcrete BLOCK_HARDENED_CONCRETE = new BlockHardenedConcrete();
    public static final BlockSteelFrame BLOCK_STEEL_FRAME = new BlockSteelFrame();
    public static final BlockCrusher BLOCK_CRUSHER = new BlockCrusher();
    public static final BlockRefractoryBrickBlock BLOCK_REFRACTORY_BRICK_BLOCK = new BlockRefractoryBrickBlock();
    public static final BlockRotaryKiln BLOCK_ROTARY_KILN = new BlockRotaryKiln();
    public static final BlockReinforcedConcrete BLOCK_REINFORCED_CONCRETE = new BlockReinforcedConcrete();
    public static final BlockSteelRebar BLOCK_STEEL_REBAR = new BlockSteelRebar();

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(BLOCK_DEEP_CLAY);
        registry.register(BLOCK_LIMESTONE);
        registry.register(BLOCK_HARDENED_CONCRETE);
        registry.register(BLOCK_STEEL_FRAME);
        registry.register(BLOCK_CRUSHER);
        registry.register(BLOCK_REFRACTORY_BRICK_BLOCK);
        registry.register(BLOCK_ROTARY_KILN);
        registry.register(ModFluids.WET_CONCRETE_BLOCK);
        registry.register(BLOCK_REINFORCED_CONCRETE);
        registry.register(BLOCK_STEEL_REBAR);
    }
}
