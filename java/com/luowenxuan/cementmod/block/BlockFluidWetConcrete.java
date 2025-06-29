package com.luowenxuan.cementmod.block;

import com.luowenxuan.cementmod.CementMod;
import com.luowenxuan.cementmod.fluids.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

import java.util.Random;

public class BlockFluidWetConcrete extends BlockFluidClassic {
    public BlockFluidWetConcrete() {
        super(ModFluids.WET_CONCRETE, Material.WATER);
        setRegistryName("wet_concrete");
        setUnlocalizedName(CementMod.MODID + ".wetconcrete");
        setHardness(100.0F);

        // 设置渲染属性
        setLightOpacity(3);
        setLightLevel(0.0F);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote) {
            entity.motionX *= 0.4;
            entity.motionZ *= 0.4;
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        scheduleHardening(world, pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);

        if (!world.isRemote && shouldHarden(world, pos)) {
            world.setBlockState(pos, BlockRegistryHandler.BLOCK_HARDENED_CONCRETE.getDefaultState());
        } else if (!world.isRemote) {
            scheduleHardening(world, pos);
        }
    }

    private void scheduleHardening(World world, BlockPos pos) {
        int delay = getHardeningTime(world, pos);
        world.scheduleUpdate(pos, this, delay);
    }

    private int getHardeningTime(World world, BlockPos pos) {
        int baseTime = world.isRaining() ? 2400 : 1200; // 1200刻 = 1分钟
        return isInWater(world, pos) || isInAcidRain(world, pos) ? baseTime / 10 : baseTime;
    }

    @Override
    public int tickRate(World world) {
        return 5; // 流体更新频率
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos);
        scheduleHardening(world, pos);
    }

    private boolean isSourceBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this && state.getValue(LEVEL) == 0;
    }

    private boolean shouldHarden(World world, BlockPos pos) {
        return isSourceBlock(world, pos) || world.rand.nextInt(100) < 5;
    }

    private boolean isInWater(World world, BlockPos pos) {
        for (BlockPos checkPos : new BlockPos[] {
                pos.north(), pos.south(), pos.east(), pos.west(), pos.up(), pos.down()
        }) {
            IBlockState neighborState = world.getBlockState(checkPos);
            Material material = neighborState.getMaterial();
            if (material == Material.WATER && neighborState.getBlock() != this) {
                return true;
            }
        }
        return false;
    }

    private boolean isInAcidRain(World world, BlockPos pos) {
        if (!world.canSeeSky(pos)) return false;
        if (!world.isRaining()) return false;
        if (!CementMod.isAcidRainInDimension(world.provider.getDimension())) return false;
        return world.getBiome(pos).getTemperature(pos) > 0.5F;
    }
}