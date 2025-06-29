package com.luowenxuan.cementmod.jei.rotarykiln;

import com.luowenxuan.cementmod.CementMod;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class RotaryKilnRecipe implements IRecipeWrapper {
    private final ItemStack input;
    private final ItemStack fuel;
    private final ItemStack output;
    private final int burnTime;
    private final boolean isFuelRecipe;

    // 普通烧制配方
    public RotaryKilnRecipe(ItemStack input, ItemStack output, int burnTime) {
        this.input = input;
        this.fuel = ItemStack.EMPTY;
        this.output = output;
        this.burnTime = burnTime;
        this.isFuelRecipe = false;
    }

    // 燃料配方
    public RotaryKilnRecipe(ItemStack fuel, int burnTime) {
        this.input = ItemStack.EMPTY;
        this.fuel = fuel;
        this.output = ItemStack.EMPTY;
        this.burnTime = burnTime;
        this.isFuelRecipe = true;
    }

    // RotaryKilnRecipe.java
    @Override
    public void getIngredients(IIngredients ingredients) {
        if (isFuelRecipe) {
            // 燃料配方：只设置燃料槽
            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(Collections.emptyList()); // 输入槽为空
            inputs.add(Collections.singletonList(fuel)); // 燃料槽有燃料
            ingredients.setInputLists(ItemStack.class, inputs);
        } else {
            // 普通配方：设置输入槽和燃料槽
            List<List<ItemStack>> inputs = new ArrayList<>();
            inputs.add(Collections.singletonList(input)); // 输入槽有物品
            inputs.add(Collections.emptyList()); // 燃料槽为空（玩家可放任意燃料）
            ingredients.setInputLists(ItemStack.class, inputs);
            ingredients.setOutput(ItemStack.class, output);
        }
    }

    public boolean isFuelRecipe() {
        return isFuelRecipe;
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        FontRenderer fontRenderer = minecraft.fontRenderer;

        if (burnTime > 0) {
            String burnTimeText = TextFormatting.GRAY + I18n.format("jei.rotary_kiln.burn_time", burnTime / 20);
            fontRenderer.drawString(burnTimeText, 45, 45, 0x555555); // 深灰色
        }
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        // 在燃料槽上方显示工具提示
        if (mouseX >= 21 && mouseX <= 37 && mouseY >= 18 && mouseY <= 34) {
            return Collections.singletonList(I18n.format("jei.rotary_kiln.fuel_slot"));
        }

        // 在输入槽上方显示工具提示
        if (mouseX >= 43 && mouseX <= 59 && mouseY >= 18 && mouseY <= 34) {
            return Collections.singletonList(I18n.format("jei.rotary_kiln.input_slot"));
        }

        // 在输出槽上方显示工具提示
        if (mouseX >= 115 && mouseX <= 131 && mouseY >= 18 && mouseY <= 34) {
            return Collections.singletonList(I18n.format("jei.rotary_kiln.output_slot"));
        }

        return Collections.emptyList(); // Java 8 兼容方式
    }
}