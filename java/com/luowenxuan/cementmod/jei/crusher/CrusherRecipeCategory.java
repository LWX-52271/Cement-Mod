package com.luowenxuan.cementmod.jei.crusher;

import com.luowenxuan.cementmod.CementMod;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class CrusherRecipeCategory implements IRecipeCategory<CrusherRecipe> {
    public static final String UID = CementMod.MODID + ".crusher";

    private final IDrawable background;
    private final IDrawableAnimated progress;
    private final String title;

    public CrusherRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation texture = new ResourceLocation(CementMod.MODID, "textures/gui/crusher.png");
        this.background = guiHelper.createDrawable(texture, 0, 0, 175, 82);
        this.title = I18n.format("jei.category.crusher");

        // 创建进度条动画
        IDrawableStatic progressStatic = guiHelper.createDrawable(texture, 0, 166, 106, 15);
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
    public void setRecipe(IRecipeLayout recipeLayout, CrusherRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        // 输入槽位置
        stacks.init(0, true, 55, 34);
        // 输出槽位置
        stacks.init(1, false, 115, 34);

        stacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        progress.draw(minecraft, 7, 60);
    }
}