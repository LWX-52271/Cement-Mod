package com.luowenxuan.cementmod.CreativeTabs;

import com.luowenxuan.cementmod.item.ItemRegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabCementMod extends CreativeTabs
{
    public static final TabCementMod TAB_CEMENT_MOD = new TabCementMod();

    public TabCementMod()
    {
        super("cementmod");
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(ItemRegistryHandler.LIME_POWDER);
    }
}
