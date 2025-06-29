package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemCementPowder extends Item
{
    public ItemCementPowder ()
    {
        this.setUnlocalizedName(CementMod.MODID + ".cementpowder");
        this.setRegistryName("cement_powder");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
