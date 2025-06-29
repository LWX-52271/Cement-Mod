package com.luowenxuan.cementmod.tiles;

import com.luowenxuan.cementmod.block.BlockCrusher;
import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.item.ItemRegistryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileCrusher extends TileEntity implements ITickable {
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private int progress;
    private int maxProgress = 200;
    private boolean structureValid = false;
    private boolean structureCheckDirty = true;
    private long lastStructureCheckTime = 0;
    private static final int CHECK_INTERVAL = 20;

    // 添加这个方法
    public void markStructureDirty() {
        this.structureCheckDirty = true;
        this.lastStructureCheckTime = 0;
        markDirty();

        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private final IItemHandler hopperHandler = new IItemHandler() {
        @Override public int getSlots() { return 1; }

        @Nonnull @Override
        public ItemStack getStackInSlot(int slot) {
            return slot == 0 ? inventory.getStackInSlot(1) : ItemStack.EMPTY;
        }

        @Nonnull @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Nonnull @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 0) return ItemStack.EMPTY;
            ItemStack output = inventory.getStackInSlot(1);
            if (output.isEmpty()) return ItemStack.EMPTY;

            int toExtract = Math.min(amount, output.getCount());
            ItemStack result = output.copy();
            result.setCount(toExtract);

            if (!simulate) {
                output.shrink(toExtract);
                if (output.isEmpty()) {
                    inventory.setStackInSlot(1, ItemStack.EMPTY);
                }
                markDirty();
            }
            return result;
        }

        @Override public int getSlotLimit(int slot) { return 64; }
    };

    @Override
    public void update() {
        long currentTime = world.getTotalWorldTime();
        if (currentTime - lastStructureCheckTime > CHECK_INTERVAL) {
            boolean lastValid = structureValid;
            structureValid = isStructureValid();
            lastStructureCheckTime = currentTime;

            if (lastValid != structureValid) {
                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }
        }

        if (!structureValid) {
            progress = 0;
            return;
        }

        if (canCrush()) {
            progress++;
            if (progress >= maxProgress) {
                crushItem();
                progress = 0;
            }
        } else {
            progress = 0;
        }
    }

    private boolean canCrush() {
        ItemStack input = inventory.getStackInSlot(0);
        return input.getItem() == Item.getItemFromBlock(BlockRegistryHandler.BLOCK_LIMESTONE);
    }

    private void crushItem() {
        ItemStack input = inventory.getStackInSlot(0);
        if (input.isEmpty()) return;

        ItemStack result = new ItemStack(ItemRegistryHandler.LIME_POWDER, 2);
        ItemStack output = inventory.getStackInSlot(1);

        if (output.isEmpty()) {
            inventory.setStackInSlot(1, result.copy());
        } else if (output.isItemEqual(result) && output.getCount() < output.getMaxStackSize()) {
            output.grow(result.getCount());
        } else {
            return;
        }

        input.shrink(1);
        markDirty();
    }

    private boolean isStructureValid() {
        // 获取破碎机朝向
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(BlockCrusher.FACING);

        // 根据朝向计算偏移方向
        EnumFacing right = facing.rotateY();
        EnumFacing back = facing.getOpposite();

        int steelFrameCount = 0;
        boolean hasBaseBlock = false;

        // ===== 底层检查 (Y-1) =====
        // 破碎机正下方 - 基础方块
        BlockPos belowPos = pos.down();
        hasBaseBlock = !world.getBlockState(belowPos).getBlock().isAir(world.getBlockState(belowPos), world, belowPos);

        // 其他三个位置 - 钢铁框架
        BlockPos[] framePositions = {
                belowPos.offset(right),      // 右侧
                belowPos.offset(back),       // 后方
                belowPos.offset(right).offset(back) // 右后方
        };

        for (BlockPos framePos : framePositions) {
            if (world.getBlockState(framePos).getBlock() == BlockRegistryHandler.BLOCK_STEEL_FRAME) {
                steelFrameCount++;
            }
        }

        if (!hasBaseBlock || steelFrameCount < 3) return false;

        // ===== 当前层检查 (Y) =====
        steelFrameCount = 0;
        boolean hasCrusher = false;

        // 破碎机自身位置
        hasCrusher = (world.getBlockState(pos).getBlock() == BlockRegistryHandler.BLOCK_CRUSHER);

        // 其他三个位置 - 钢铁框架
        BlockPos[] framePositionsCurrent = {
                pos.offset(right),      // 右侧
                pos.offset(back),       // 后方
                pos.offset(right).offset(back) // 右后方
        };

        for (BlockPos framePos : framePositionsCurrent) {
            if (world.getBlockState(framePos).getBlock() == BlockRegistryHandler.BLOCK_STEEL_FRAME) {
                steelFrameCount++;
            }
        }

        return hasCrusher && steelFrameCount >= 3;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        progress = compound.getInteger("Progress");
        structureValid = compound.getBoolean("StructureValid");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Inventory", inventory.serializeNBT());
        compound.setInteger("Progress", progress);
        compound.setBoolean("StructureValid", structureValid);
        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(hopperHandler);
            }
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(capability, facing);
    }

    public int getProgressScaled(int scale) {
        return progress * scale / maxProgress;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setBoolean("StructureValid", structureValid);
        tag.setInteger("Progress", progress);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        structureValid = tag.getBoolean("StructureValid");
        progress = tag.getInteger("Progress");
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }
}