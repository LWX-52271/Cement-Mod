package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockLimestone extends Block
{
    public BlockLimestone()
    {
        super(Material.ROCK);
        this.setUnlocalizedName(CementMod.MODID + ".limestone");
        this.setRegistryName("limestone");
        this.setHarvestLevel("pickaxe",2);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
