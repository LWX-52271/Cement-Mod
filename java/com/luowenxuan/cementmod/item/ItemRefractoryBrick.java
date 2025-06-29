package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemRefractoryBrick extends Item
{
    public ItemRefractoryBrick()
    {
        this.setUnlocalizedName(CementMod.MODID + ".refractorybrick");
        this.setRegistryName("refractory_brick");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}

