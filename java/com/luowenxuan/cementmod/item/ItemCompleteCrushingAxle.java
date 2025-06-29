package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemCompleteCrushingAxle extends Item
{
    public ItemCompleteCrushingAxle()
    {
        this.setUnlocalizedName(CementMod.MODID + ".completecrushingaxle");
        this.setRegistryName("complete_crushing_axle");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
