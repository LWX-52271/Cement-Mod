package com.luowenxuan.cementmod.gui;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.tiles.TileCrusher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiCrusher extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CementMod.MODID, "textures/gui/crusher.png");
    private TileCrusher te;

    public GuiCrusher(TileCrusher te, ContainerCrusher container) {
        super(container);
        this.te = te;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // 绘制进度条
        int progress = te.getProgressScaled(107);
        drawTexturedModalRect(guiLeft + 7, guiTop + 60, 0, 166, progress, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("tile.crusher.name");
        fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, 0x404040);
    }
}
