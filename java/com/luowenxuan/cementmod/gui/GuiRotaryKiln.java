package com.luowenxuan.cementmod.gui;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.tiles.TileRotaryKiln;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiRotaryKiln extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CementMod.MODID, "textures/gui/rotary_kiln.png");
    private final TileRotaryKiln tileEntity;

    public GuiRotaryKiln(TileRotaryKiln te, ContainerRotaryKiln container) {
        super(container);
        this.tileEntity = te;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // 绘制燃烧进度
        if (tileEntity.getBurnTime() > 0) {
            int burnHeight = tileEntity.getBurnTimeScaled(14);
            drawTexturedModalRect(guiLeft + 139, guiTop + 72 - burnHeight,
                    0, 190 - burnHeight, 18, burnHeight);
        }

        // 绘制烧制进度
        int cookProgress = tileEntity.getCookProgressScaled(100);
        drawTexturedModalRect(guiLeft + 51, guiTop + 40, 0, 166, cookProgress, 5);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = I18n.format("tile.rotary_kiln.name");
        fontRenderer.drawString(title, xSize / 2 - fontRenderer.getStringWidth(title) / 2, 6, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
