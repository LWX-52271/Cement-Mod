package com.luowenxuan.cementmod.gui;

import com.luowenxuan.cementmod.tiles.TileRotaryKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRotaryKiln extends Container {
    private final TileRotaryKiln tileEntity;
    private int lastBurnTime = 0;
    private int lastCookTime = 0;
    private int lastCurrentBurnTime = 0;

    public ContainerRotaryKiln(InventoryPlayer playerInv, TileRotaryKiln te) {
        this.tileEntity = te;
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // 输入槽 (0)
        addSlotToContainer(new SlotItemHandler(inventory, 0, 43, 56));

        // 燃料槽 (1)
        addSlotToContainer(new SlotItemHandler(inventory, 1, 21, 56));

        // 输出槽 (2) - 不可输入
        addSlotToContainer(new SlotItemHandler(inventory, 2, 115, 56) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false; // 输出槽不能放入物品
            }
        });

        // 玩家物品栏 (9x3)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // 玩家快捷栏 (9x1)
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.getDistanceSq(player.posX, player.posY, player.posZ) <= 64.0D;
    }

    // 同步容器数据 - 修正后的方法
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        // 获取当前值
        int currentBurnTime = tileEntity.getBurnTime();
        int cookTime = tileEntity.getCookTime();
        int currentBurnDuration = tileEntity.getCurrentBurnTime();

        // 检查是否有变化
        boolean changed = false;
        if (lastBurnTime != currentBurnTime ||
                lastCookTime != cookTime ||
                lastCurrentBurnTime != currentBurnDuration) {

            changed = true;
            lastBurnTime = currentBurnTime;
            lastCookTime = cookTime;
            lastCurrentBurnTime = currentBurnDuration;
        }

        // 发送更新给所有监听器
        if (changed) {
            for (IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(this, 0, currentBurnTime);
                listener.sendWindowProperty(this, 1, cookTime);
                listener.sendWindowProperty(this, 2, currentBurnDuration);
            }
        }
    }

    // 接收同步数据
    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0: tileEntity.setBurnTime(data); break;
            case 1: tileEntity.setCookTime(data); break;
            case 2: tileEntity.setCurrentBurnTime(data); break;
        }
    }

    // 物品转移逻辑
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // 从机器槽位移动到玩家物品栏
            if (index < 3) {
                if (!mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            // 从玩家物品栏移动到机器槽位
            else {
                // 尝试放入输入槽
                if (TileRotaryKiln.isInput(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 尝试放入燃料槽
                else if (TileRotaryKiln.isFuel(itemstack1)) {
                    if (!mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 从快捷栏移动到主物品栏
                else if (index >= 3 && index < 30) {
                    if (!mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 从主物品栏移动到快捷栏
                else if (index >= 30 && index < 39) {
                    if (!mergeItemStack(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
