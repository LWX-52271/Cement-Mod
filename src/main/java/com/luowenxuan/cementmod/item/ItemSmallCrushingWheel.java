package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemSmallCrushingWheel extends Item
{
    public ItemSmallCrushingWheel ()
    {
        this.setUnlocalizedName(CementMod.MODID + ".smallcrushingwheel");
        this.setRegistryName("small_crushing_wheel");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
