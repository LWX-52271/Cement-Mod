package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemRawMixture extends Item
{
    public ItemRawMixture()
    {
        this.setUnlocalizedName(CementMod.MODID + ".rawmixture");
        this.setRegistryName("raw_mixture");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
