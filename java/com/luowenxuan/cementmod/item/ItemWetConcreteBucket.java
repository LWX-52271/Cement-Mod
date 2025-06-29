package com.luowenxuan.cementmod.item;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import com.luowenxuan.cementmod.fluids.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;

public class ItemWetConcreteBucket extends Item {
    public ItemWetConcreteBucket() {
        this.setUnlocalizedName(CementMod.MODID + ".wetconcretebucket");
        this.setRegistryName("wet_concrete_bucket");
        this.setMaxStackSize(1);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        RayTraceResult raytrace = rayTrace(world, player, true);

        if (raytrace == null || raytrace.typeOfHit != RayTraceResult.Type.BLOCK) {
            CementMod.logger.debug("未命中方块或无效的射线追踪结果");
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }

        BlockPos pos = raytrace.getBlockPos();
        EnumFacing side = raytrace.sideHit;
        BlockPos placePos = pos.offset(side);

        // 1. 确保玩家可以编辑该位置
        if (!player.canPlayerEdit(placePos, side, itemStack)) {
            CementMod.logger.debug("玩家无法编辑位置: " + placePos);
            return new ActionResult<>(EnumActionResult.FAIL, itemStack);
        }

        // 2. 检查目标位置是否可以放置流体
        IBlockState targetState = world.getBlockState(placePos);
        Block targetBlock = targetState.getBlock();

        boolean canPlace = world.isAirBlock(placePos) ||
                targetBlock.isReplaceable(world, placePos) ||
                targetBlock instanceof IFluidBlock;

        if (!canPlace) {
            CementMod.logger.debug("目标位置不可放置流体: " + placePos + ", 方块: " + targetBlock);
            return new ActionResult<>(EnumActionResult.FAIL, itemStack);
        }

        // 3. 尝试使用Forge的流体放置方法
        FluidStack fluidStack = new FluidStack(ModFluids.WET_CONCRETE, 1000);
        boolean placed = FluidUtil.tryPlaceFluid(player, world, placePos, itemStack, fluidStack).isSuccess();

        if (placed) {
            CementMod.logger.info("成功放置流体在位置: " + placePos);
            // 播放放置声音
            world.playSound(player, placePos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!player.capabilities.isCreativeMode) {
                return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(net.minecraft.init.Items.BUCKET));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        // 4. 如果Forge方法失败，尝试手动放置
        CementMod.logger.debug("Forge流体放置方法失败，尝试手动放置");
        if (world.mayPlace(ModFluids.WET_CONCRETE_BLOCK, placePos, false, side, player)) {
            IBlockState fluidState = ModFluids.WET_CONCRETE_BLOCK.getDefaultState();
            if (world.setBlockState(placePos, fluidState)) {
                CementMod.logger.info("手动放置流体成功在位置: " + placePos);

                // 播放放置声音
                world.playSound(player, placePos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!player.capabilities.isCreativeMode) {
                    return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(net.minecraft.init.Items.BUCKET));
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }

        CementMod.logger.debug("所有放置方法均失败");
        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }
}
