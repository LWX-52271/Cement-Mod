package com.luowenxuan.cementmod.jei.crusher;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CrusherRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public CrusherRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }
}