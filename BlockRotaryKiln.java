package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import com.luowenxuan.cementmod.tiles.TileRotaryKiln;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

// 文件路径：src/main/java/com/yourname/cementmod/blocks/BlockRotaryKiln.java
public class BlockRotaryKiln extends BlockContainer {
    // 添加方向属性
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockRotaryKiln() {
        super(Material.IRON);
        setRegistryName("rotary_kiln");
        setUnlocalizedName(CementMod.MODID + ".rotarykiln");
        setHardness(3.5F);
        setResistance(20.0F);
        setLightLevel(0.8F);// 工作时发光
        setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
        // 设置默认状态（朝北）
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }



    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileRotaryKiln();
    }

    // 添加粒子效果
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRotaryKiln ) {
            TileRotaryKiln kiln = (TileRotaryKiln) te;
            // 生成火焰粒子
            for (int i = 0; i < 3; i++) {
                double px = pos.getX() + 0.5 + (rand.nextDouble() - 0.5) * 0.5;
                double py = pos.getY() + 0.1;
                double pz = pos.getZ() + 0.5 + (rand.nextDouble() - 0.5) * 0.5;
                world.spawnParticle(EnumParticleTypes.FLAME, px, py, pz, 0, 0.02, 0);
            }

            // 生成烟雾粒子
            if (rand.nextFloat() < 0.1f) {
                double px = pos.getX() + 0.5;
                double py = pos.getY() + 1.0;
                double pz = pos.getZ() + 0.5;
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, px, py, pz, 0, 0.1, 0);
            }
        }
    }

    // 右键打开GUI
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(CementMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    // 渲染类型
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    // 添加方向状态管理
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    // 当玩家放置方块时，根据玩家朝向设置方向
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer) {
        // 获取玩家的水平朝向（忽略上下看）
        EnumFacing playerFacing = placer.getHorizontalFacing();
        // 返回与玩家朝向相反的方向（这样方块的正面会朝向玩家）
        return getDefaultState().withProperty(FACING, playerFacing.getOpposite());
    }
}
