package com.luowenxuan.cementmod.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockMachine extends ItemBlock {
    private final String tooltipKey;

    public ItemBlockMachine(Block block, String tooltipKey) {
        super(block);
        this.tooltipKey = tooltipKey;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world,
                               List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(net.minecraft.client.resources.I18n.format(tooltipKey));
    }
}