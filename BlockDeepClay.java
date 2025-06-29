package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDeepClay extends Block
{
    public BlockDeepClay()
    {
        super(Material.CLAY);
        this.setUnlocalizedName(CementMod.MODID + ".deepclay");
        this.setRegistryName("deep_clay");
        this.setHarvestLevel("shovel",2);
        this.setHardness(0.5F);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
        this.setSoundType(SoundType.GLASS);
    }
}
