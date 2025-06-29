package com.luowenxuan.cementmod.entities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityConcreteDebris extends EntityFallingBlock {
    // 坍塌粒子计数器
    private int particleCounter = 0;
    private final IBlockState blockState; // 存储方块状态

    public EntityConcreteDebris(World world, double x, double y, double z, IBlockState state) {
        super(world, x, y, z, state);
        this.blockState = state; // 保存传入的方块状态
        this.fallTime = 0;
        this.shouldDropItem = false;
        this.setNoGravity(false);
        this.motionY = -0.5; // 下落速度
    }

    // 获取方块状态的方法
    public IBlockState getBlockState() {
        return blockState;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        // 生成粒子效果
        if (world.isRemote && particleCounter++ % 3 == 0) {
            // 使用保存的 blockState 获取状态ID
            world.spawnParticle(
                    EnumParticleTypes.BLOCK_CRACK,
                    posX, posY, posZ,
                    0, 0, 0,
                    Block.getStateId(getBlockState())
            );
        }

        // 落地处理
        if (onGround) {
            handleImpact();
        }
    }

    // 落地效果处理
    private void handleImpact() {
        // 1. 播放撞击声
        playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8F, 0.9F);

        // 2. 生成灰尘粒子
        if (!world.isRemote) {
            for (int i = 0; i < 8; ++i) {
                world.spawnParticle(
                        EnumParticleTypes.BLOCK_DUST,
                        posX + rand.nextGaussian() * 0.5,
                        posY + 0.1,
                        posZ + rand.nextGaussian() * 0.5,
                        0.0, 0.0, 0.0,
                        Block.getStateId(Blocks.STONE.getDefaultState())
                );
            }
        }

        // 3. 移除实体
        setDead();
    }

    // 禁用伤害
    @Override
    public void fall(float distance, float damageMultiplier) {}
}