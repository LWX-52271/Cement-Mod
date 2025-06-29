package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemLimePowder extends Item
{
    public ItemLimePowder ()
    {
        this.setUnlocalizedName(CementMod.MODID + ".limepowder");
        this.setRegistryName("lime_powder");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
