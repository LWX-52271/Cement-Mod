package com.luowenxuan.cementmod.jei.crusher;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.item.ItemRegistryHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrusherRecipeMaker {
    public static List<CrusherRecipe> getRecipes() {
        List<CrusherRecipe> recipes = new ArrayList<>();

        // 添加石灰石破碎配方
        recipes.add(new CrusherRecipe(
                new ItemStack(BlockRegistryHandler.BLOCK_LIMESTONE),
                new ItemStack(ItemRegistryHandler.LIME_POWDER, 2)
        ));

        // 添加其他破碎配方...
        // recipes.add(new CrusherRecipe(...));

        return recipes;
    }
}