package com.luowenxuan.cementmod.jei.rotarykiln;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.util.Arrays;

public class RotaryKilnRecipeCategory implements IRecipeCategory<RotaryKilnRecipe> {
    public static final String UID = CementMod.MODID + ".rotary_kiln";

    private final IDrawable background;
    private final IDrawableAnimated flame;
    private final IDrawableAnimated progress;
    private final String title;
    private final IDrawable icon;

    public RotaryKilnRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation texture = new ResourceLocation(CementMod.MODID, "textures/gui/rotary_kiln.png");
        this.background = guiHelper.createDrawable(texture, 0, 0, 175, 82);
        this.title = I18n.format("jei.category.rotary_kiln");
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(BlockRegistryHandler.BLOCK_ROTARY_KILN));

        // 火焰动画
        IDrawableStatic flameStatic = guiHelper.createDrawable(texture, 0, 172, 16, 18);
        this.flame = guiHelper.createAnimatedDrawable(flameStatic, 300, IDrawableAnimated.StartDirection.TOP, true);

        // 进度条动画
        IDrawableStatic progressStatic = guiHelper.createDrawable(texture, 0, 166, 100, 5);
        this.progress = guiHelper.createAnimatedDrawable(progressStatic, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getModName() {
        return CementMod.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RotaryKilnRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        // 输入槽位置 (生料混合物)
        stacks.init(0, true, 42, 55);

        // 燃料槽位置
        stacks.init(1, true, 20, 55);

        // 输出槽位置 (水泥粉)
        stacks.init(2, false, 114, 55);

        // 设置配方内容
        stacks.set(ingredients);

        // 如果是燃料配方，隐藏输出槽
        if (recipe.isFuelRecipe()) {
            stacks.set(2, ItemStack.EMPTY);
        }
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        // 绘制火焰动画
        flame.draw(minecraft, 139, 54);

        // 绘制进度条动画
        progress.draw(minecraft, 51, 40);
    }
}