package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static com.luowenxuan.cementmod.block.BlockRegistryHandler.BLOCK_STEEL_REBAR;

public class BlockSteelRebar extends Block {
    // 使用 1.12.2 兼容的方式定义水平方向
    public static final PropertyDirection FACING = PropertyDirection.create("facing",
            facing -> facing != EnumFacing.UP && facing != EnumFacing.DOWN);

    public BlockSteelRebar() {
        super(Material.IRON);
        setRegistryName("steel_rebar");
        setUnlocalizedName("steel_rebar");
        setHardness(3.0F);
        setResistance(20.0F);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    // 1.12.2 兼容的元数据转换
    @Override
    public IBlockState getStateFromMeta(int meta) {
        // 水平方向索引：0-北, 1-东, 2-南, 3-西
        EnumFacing facing = EnumFacing.getFront(meta);

        // 确保只返回水平方向
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.NORTH;
        }
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // 直接返回方向的索引值
        return state.getValue(FACING).getIndex();
    }

    // 放置逻辑增强
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer) {
        // 获取玩家水平朝向
        EnumFacing playerFacing = placer.getHorizontalFacing();

        // 如果放置面是垂直方向（上/下），使用玩家水平朝向
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return getDefaultState().withProperty(FACING, playerFacing);
        }

        // 否则使用放置面的方向
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public static boolean isPositionReinforced(IBlockAccess world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = pos.add(x, y, z);
                    if (world.getBlockState(checkPos).getBlock() instanceof BlockSteelRebar) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // 在 BlockSteelRebar.java 中添加
    public static boolean isReinforced(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (world.getBlockState(pos.offset(facing)).getBlock() == BLOCK_STEEL_REBAR) {
                return true;
            }
        }
        return false;
    }
}