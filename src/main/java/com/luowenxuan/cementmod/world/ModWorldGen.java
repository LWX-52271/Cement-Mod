package com.luowenxuan.cementmod.world;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == 0) { // 主世界
            generateOverworld(random, chunkX, chunkZ, world);
        }
    }

    private void generateOverworld(Random rand, int chunkX, int chunkZ, World world) {
        // 生成石灰石（类似安山岩生成）
        generateOre(BlockRegistryHandler.BLOCK_LIMESTONE.getDefaultState(), world, rand,
                chunkX << 4, chunkZ << 4, 5, 16, 4 + rand.nextInt(4), 20);

        // 生成深层黏土（Y<40）
        generateOre(BlockRegistryHandler.BLOCK_DEEP_CLAY.getDefaultState(), world, rand,
                chunkX << 4, chunkZ << 4, 1, 40, 6 + rand.nextInt(6), 30);
    }

    private void generateOre(IBlockState ore, World world, Random rand,
                             int x, int z, int minY, int maxY, int size, int chances) {
        for (int i = 0; i < chances; i++) {
            BlockPos pos = new BlockPos(
                    x + rand.nextInt(16),
                    minY + rand.nextInt(maxY - minY),
                    z + rand.nextInt(16)
            );
            new WorldGenMinable(ore, size).generate(world, rand, pos);
        }
    }
}