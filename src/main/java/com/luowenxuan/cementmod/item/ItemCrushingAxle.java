package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemCrushingAxle extends Item
{
    public ItemCrushingAxle()
    {
        this.setUnlocalizedName(CementMod.MODID + ".crushingaxle");
        this.setRegistryName("crushing_axle");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
