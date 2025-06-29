package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.item.Item;

public class ItemIronPlate extends Item
{
    public ItemIronPlate ()
    {
        this.setUnlocalizedName(CementMod.MODID + ".ironplate");
        this.setRegistryName("iron_plate");
        this.setMaxStackSize(64);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }
}
