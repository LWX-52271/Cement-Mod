package com.luowenxuan.cementmod.tiles;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import com.luowenxuan.cementmod.item.ItemRegistryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class TileRotaryKiln extends TileEntity implements ITickable {
    // 物品槽：0=输入槽, 1=燃料槽, 2=输出槽
    private final ItemStackHandler inventory = new ItemStackHandler(3);
    private int burnTime;          // 当前燃烧时间
    private int currentBurnTime;   // 当前燃料可燃烧时间
    private int cookTime;          // 当前烧制进度

    // 结构验证缓存优化
    private boolean structureValid = false;
    private boolean structureCheckDirty = true; // 初始需要检查
    private int ticksSinceLastCheck = 0;
    private static final int CHECK_INTERVAL = 20; // 20刻=1秒检查一次

    @Override
    public void update() {
        // 优化结构检查频率
        ticksSinceLastCheck++;
        if (structureCheckDirty && ticksSinceLastCheck >= CHECK_INTERVAL) {
            boolean lastValid = structureValid;
            structureValid = isStructureValid();
            structureCheckDirty = false;
            ticksSinceLastCheck = 0;

            // 结构状态变化时通知客户端
            if (lastValid != structureValid) {
                IBlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }
        }

        if (world.isRemote) return;

        // 只在结构有效时处理逻辑
        if (!structureValid) {
            cookTime = 0;
            return;
        }

        // 燃料检测逻辑
        if (burnTime <= 0 && canConsumeFuel()) {
            consumeFuel();
        }

        // 烧制逻辑
        if (canSmelt()) {
            cookTime++;
            if (cookTime >= 200) { // 10秒烧制时间
                smeltItem();
                cookTime = 0;
            }
        } else {
            cookTime = 0; // 重置进度
        }

        // 燃烧时间递减
        if (burnTime > 0) {
            burnTime--;
        }

        markDirty(); // 标记数据需要保存
    }

    // 检查是否可以消耗燃料
    private boolean canConsumeFuel() {
        ItemStack fuelStack = inventory.getStackInSlot(1);
        return getItemBurnTime(fuelStack) > 0;
    }

    // 消耗燃料
    private void consumeFuel() {
        ItemStack fuelStack = inventory.getStackInSlot(1);
        burnTime = currentBurnTime = getItemBurnTime(fuelStack);

        // 处理容器物品（如桶）
        if (fuelStack.getItem().hasContainerItem(fuelStack)) {
            inventory.setStackInSlot(1, fuelStack.getItem().getContainerItem(fuelStack));
        } else {
            fuelStack.shrink(1);
        }
    }

    // 获取燃料燃烧时间
    private int getItemBurnTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;

        // 自定义燃料值
        if (stack.getItem() == Items.LAVA_BUCKET) return 20000; // 1000秒
        if (stack.getItem() == Items.COAL) return 1600;         // 80秒
        if (stack.getItem() == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            return 16000; // 800秒

        return TileEntityFurnace.getItemBurnTime(stack); // 默认值
    }

    // 检查是否可以烧制
    private boolean canSmelt() {
        if (inventory.getStackInSlot(0).isEmpty()) return false;
        if (burnTime <= 0) return false; // 需要燃料

        // 检查输入是否为生料混合物
        ItemStack input = inventory.getStackInSlot(0);
        if (input.getItem() != ItemRegistryHandler.RAW_MIXTURE) return false;

        // 检查输出槽是否有空间
        ItemStack output = inventory.getStackInSlot(2);
        if (output.isEmpty()) return true;
        return output.getCount() < output.getMaxStackSize() &&
                output.getItem() == ItemRegistryHandler.CEMENT_POWDER;
    }

    // 执行烧制
    private void smeltItem() {
        inventory.extractItem(0, 1, false); // 消耗1个生料混合物
        ItemStack output = inventory.getStackInSlot(2);

        if (output.isEmpty()) {
            inventory.setStackInSlot(2, new ItemStack(ItemRegistryHandler.CEMENT_POWDER, 1));
        } else if (output.getItem() == ItemRegistryHandler.CEMENT_POWDER) {
            output.grow(1);
        }
    }

    // 修复后的结构验证方法 - 支持任意方向的1x3结构
    private boolean isStructureValid() {
        MutableBlockPos checkPos = new MutableBlockPos();
        int centerX = pos.getX();
        int centerY = pos.getY();
        int centerZ = pos.getZ();

        // 尝试X轴方向
        if (isValidStructureAlongAxis(centerX, centerY, centerZ, true)) {
            return true;
        }

        // 尝试Z轴方向
        if (isValidStructureAlongAxis(centerX, centerY, centerZ, false)) {
            return true;
        }

        return false;
    }

    // 检查特定轴方向的结构是否有效
    private boolean isValidStructureAlongAxis(int centerX, int centerY, int centerZ, boolean xAxis) {
        MutableBlockPos checkPos = new MutableBlockPos();
        int refractoryBrickCount = 0;
        int rotaryKilnCount = 0;

        // 检查支撑层（Y-1）
        for (int offset = -1; offset <= 1; offset++) {
            if (xAxis) {
                checkPos.setPos(centerX + offset, centerY - 1, centerZ);
            } else {
                checkPos.setPos(centerX, centerY - 1, centerZ + offset);
            }

            IBlockState state = world.getBlockState(checkPos);
            if (state.getBlock() == BlockRegistryHandler.BLOCK_REFRACTORY_BRICK_BLOCK) {
                refractoryBrickCount++;
            }
        }

        // 必须有三个防火砖
        if (refractoryBrickCount < 3) {
            return false;
        }

        // 检查回转窑层（Y）
        for (int offset = -1; offset <= 1; offset++) {
            if (xAxis) {
                checkPos.setPos(centerX + offset, centerY, centerZ);
            } else {
                checkPos.setPos(centerX, centerY, centerZ + offset);
            }

            IBlockState state = world.getBlockState(checkPos);
            if (state.getBlock() == BlockRegistryHandler.BLOCK_ROTARY_KILN) {
                rotaryKilnCount++;
            }
        }

        // 必须有三个回转窑（包括自身）
        return rotaryKilnCount >= 3;
    }

    // ----- 数据保存与同步 -----
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        burnTime = compound.getInteger("BurnTime");
        cookTime = compound.getInteger("CookTime");
        currentBurnTime = compound.getInteger("CurrentBurnTime");
        structureCheckDirty = true; // 加载后需要检查结构
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("Inventory", inventory.serializeNBT());
        compound.setInteger("BurnTime", burnTime);
        compound.setInteger("CookTime", cookTime);
        compound.setInteger("CurrentBurnTime", currentBurnTime);
        return compound;
    }

    // ----- 进度获取方法（用于GUI显示）-----
    public int getBurnTimeScaled(int scale) {
        if (currentBurnTime == 0) return 0;
        return burnTime * scale / currentBurnTime;
    }

    public int getCookProgressScaled(int scale) {
        return cookTime * scale / 200;
    }

    // ----- 物品槽访问 -----
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
        }
        return super.getCapability(capability, facing);
    }

    public boolean isBurning(){
        return burnTime > 0;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCurrentBurnTime() {
        return currentBurnTime;
    }

    // 在 TileRotaryKiln.java 中添加
    public static boolean isInput(ItemStack stack) {
        return stack.getItem() == ItemRegistryHandler.RAW_MIXTURE;
    }

    public static boolean isFuel(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setCurrentBurnTime(int currentBurnTime) {
        this.currentBurnTime = currentBurnTime;
    }

    // 添加结构检查标记方法
    public void markStructureDirty() {
        structureCheckDirty = true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        structureCheckDirty = true; // 加载时标记需要检查结构
    }
}