package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.CreativeTabs.TabCementMod;
import com.luowenxuan.cementmod.tiles.TileCrusher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCrusher extends BlockContainer
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockCrusher()
    {
        super(Material.IRON);
        this.setUnlocalizedName(CementMod.MODID + ".crusher");
        this.setRegistryName("crusher");
        this.setHarvestLevel("pickaxe",2);
        this.setHardness(3.0F);
        this.setResistance(15.0F);
        this.setCreativeTab(TabCementMod.TAB_CEMENT_MOD);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    // 新增：在方块放置时设置朝向
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta,
                                            EntityLivingBase placer) {
        // 获取玩家放置时的水平朝向（不包括上下）
        EnumFacing horizontalFacing = placer.getHorizontalFacing();
        // 取反方向（使方块正面朝向玩家）
        return getDefaultState().withProperty(FACING, horizontalFacing.getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileCrusher();
    }

    // 右键打开GUI
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(CementMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    // 确保声明方块拥有 TileEntity
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    // 渲染类型
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity te = world.getTileEntity(pos);
        return te != null && te.receiveClientEvent(id, param);
    }

    // 当邻居方块更新时，通知TileEntity检查结构
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileCrusher) {
            ((TileCrusher) te).markStructureDirty();
        }
    }
}

