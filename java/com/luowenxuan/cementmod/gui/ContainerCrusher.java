package com.luowenxuan.cementmod.gui;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.tiles.TileCrusher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCrusher extends Container {
    private TileCrusher te;

    public ContainerCrusher(IInventory playerInv, TileCrusher te) {
        if (te == null) {
            throw new IllegalStateException("TileEntity is null!");
        }
        this.te = te;
        IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // 输入槽 (0)
        addSlotToContainer(new SlotItemHandler(inventory, 0, 56, 35));

        // 输出槽 (1) - 不可输入
        addSlotToContainer(new SlotItemHandler(inventory, 1, 116, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
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
        return true;
    }

    // 修复后的转移物品逻辑
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int containerSlots = 2; // 机器槽位数量

            // 机器槽位 -> 玩家背包
            if (index < containerSlots) {
                // 输出槽(1)可以提取到任何背包位置
                if (index == 1) {
                    if (!this.mergeItemStack(itemstack1, containerSlots, containerSlots + 36, true)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 输入槽(0)只能转移到玩家背包（不能放回输出槽）
                else if (index == 0) {
                    if (!this.mergeItemStack(itemstack1, containerSlots, containerSlots + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            // 玩家背包 -> 机器输入槽
            else {
                // 只允许放入石灰石
                if (itemstack1.getItem() == Item.getItemFromBlock(BlockRegistryHandler.BLOCK_LIMESTONE)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
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