package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockHardenedConcrete extends Block
{
    public BlockHardenedConcrete()
    {
        super(Material.ROCK);
        this.setUnlocalizedName(CementMod.MODID + ".hardenedconcrete");
        this.setRegistryName("hardened_concrete");
        this.setHarvestLevel("pickaxe",3);
        this.setHardness(2.0F);
        this.setResistance(12.0F);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
