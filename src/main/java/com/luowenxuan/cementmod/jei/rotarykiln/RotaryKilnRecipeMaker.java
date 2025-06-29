package com.luowenxuan.cementmod.jei.rotarykiln;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.item.ItemRegistryHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import java.util.ArrayList;
import java.util.List;

public class RotaryKilnRecipeMaker {
    public static List<RotaryKilnRecipe> getRecipes() {
        List<RotaryKilnRecipe> recipes = new ArrayList<>();

        // 添加主要烧制配方 - 生料混合物 -> 水泥粉
        recipes.add(new RotaryKilnRecipe(
                new ItemStack(ItemRegistryHandler.RAW_MIXTURE),
                new ItemStack(ItemRegistryHandler.CEMENT_POWDER),
                200 // 10秒烧制时间
        ));

        // 添加燃料配方
        addFuelRecipes(recipes);

        return recipes;
    }

    private static void addFuelRecipes(List<RotaryKilnRecipe> recipes) {
        // 1.12.2 兼容方式 - 手动添加常见燃料

        // 煤炭燃料
        addFuelRecipe(recipes, new ItemStack(Items.COAL), 1600); // 80秒燃烧时间

        // 木炭燃料
        addFuelRecipe(recipes, new ItemStack(Items.COAL, 1, 1), 1600); // 80秒燃烧时间

        // 煤炭块燃料
        addFuelRecipe(recipes, new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK)), 16000); // 800秒燃烧时间

        // 岩浆桶燃料
        addFuelRecipe(recipes, new ItemStack(Items.LAVA_BUCKET), 20000); // 1000秒燃烧时间

        // 木板燃料
        addFuelRecipe(recipes, new ItemStack(Blocks.PLANKS), 300);

        // 木棍燃料
        addFuelRecipe(recipes, new ItemStack(Items.STICK), 100);

        // 原木燃料
        addFuelRecipe(recipes, new ItemStack(Blocks.LOG), 300);
        addFuelRecipe(recipes, new ItemStack(Blocks.LOG2), 300);

        // 烈焰棒燃料
        addFuelRecipe(recipes, new ItemStack(Items.BLAZE_ROD), 2400);

        // 其他常见燃料
        addFuelRecipe(recipes, new ItemStack(Items.BOAT), 400);
        addFuelRecipe(recipes, new ItemStack(Blocks.SAPLING), 100);
        addFuelRecipe(recipes, new ItemStack(Blocks.DEADBUSH), 100);
        addFuelRecipe(recipes, new ItemStack(Items.BOW), 300);
        addFuelRecipe(recipes, new ItemStack(Items.FISHING_ROD), 300);
        addFuelRecipe(recipes, new ItemStack(Blocks.WOODEN_SLAB), 150);
        addFuelRecipe(recipes, new ItemStack(Blocks.WOOL), 100);
        addFuelRecipe(recipes, new ItemStack(Items.CHORUS_FRUIT_POPPED), 200);
    }

    private static void addFuelRecipe(List<RotaryKilnRecipe> recipes, ItemStack fuelStack, int burnTime) {
        int actualBurnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
        if (actualBurnTime > 0) {
            recipes.add(new RotaryKilnRecipe(fuelStack, actualBurnTime));
        }
    }
}