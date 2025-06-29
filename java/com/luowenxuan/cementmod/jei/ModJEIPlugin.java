package com.luowenxuan.cementmod.jei;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.jei.crusher.CrusherRecipe;
import com.luowenxuan.cementmod.jei.crusher.CrusherRecipeCategory;
import com.luowenxuan.cementmod.jei.crusher.CrusherRecipeMaker;
import com.luowenxuan.cementmod.jei.rotarykiln.RotaryKilnRecipe;
import com.luowenxuan.cementmod.jei.rotarykiln.RotaryKilnRecipeCategory;
import com.luowenxuan.cementmod.jei.rotarykiln.RotaryKilnRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import java.util.List;

@JEIPlugin
public class ModJEIPlugin implements IModPlugin {

    private IIngredientRegistry ingredientRegistry;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        // 注册破碎机配方类别
        registry.addRecipeCategories(
                new CrusherRecipeCategory(guiHelper)
        );

        // 注册回转窑配方类别
        registry.addRecipeCategories(
                new RotaryKilnRecipeCategory(guiHelper)
        );
    }

    @Override
    public void register(IModRegistry registry) {
        // 获取原料注册表
        this.ingredientRegistry = registry.getIngredientRegistry();

        // 添加破碎机配方
        List<CrusherRecipe> crusherRecipes = CrusherRecipeMaker.getRecipes();
        registry.addRecipes(crusherRecipes, CrusherRecipeCategory.UID);
        registry.addRecipeCatalyst(
                new ItemStack(BlockRegistryHandler.BLOCK_CRUSHER),
                CrusherRecipeCategory.UID
        );

        // 添加回转窑配方
        List<RotaryKilnRecipe> kilnRecipes = RotaryKilnRecipeMaker.getRecipes();
        registry.addRecipes(kilnRecipes, RotaryKilnRecipeCategory.UID);
        registry.addRecipeCatalyst(
                new ItemStack(BlockRegistryHandler.BLOCK_ROTARY_KILN),
                RotaryKilnRecipeCategory.UID
        );

        // 添加配方点击区域
        registry.addRecipeClickArea(
                com.luowenxuan.cementmod.gui.GuiRotaryKiln.class,
                80, 35, 22, 15,
                RotaryKilnRecipeCategory.UID
        );
    }
}