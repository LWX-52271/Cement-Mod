package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockRefractoryBrickBlock extends Block
{
    public BlockRefractoryBrickBlock()
    {
        super(Material.ROCK);
        this.setUnlocalizedName(CementMod.MODID + ".refractorybrickblock");
        this.setRegistryName("refractory_brick_block");
        this.setHarvestLevel("pickaxe",1);
        this.setHardness(2.0F);
        this.setResistance(15.0F);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
