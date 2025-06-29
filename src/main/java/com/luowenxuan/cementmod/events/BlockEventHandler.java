package com.luowenxuan.cementmod.events;

import com.luowenxuan.cementmod.util.StructureIntegrity;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {
    // 方块破坏事件
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Block block = world.getBlockState(pos).getBlock();

        if (StructureIntegrity.STRUCTURAL_BLOCKS.contains(block)) {
            StructureIntegrity.checkStructuralIntegrity(world, pos);
        }
    }

    // 爆炸事件
    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        World world = event.getWorld();
        for (BlockPos pos : event.getAffectedBlocks()) {
            Block block = world.getBlockState(pos).getBlock();
            if (StructureIntegrity.STRUCTURAL_BLOCKS.contains(block)) {
                StructureIntegrity.checkStructuralIntegrity(world, pos);
            }
        }
    }

    // 方块更新事件（检测支撑变化）
    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        // 只检查结构块
        if (!StructureIntegrity.STRUCTURAL_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
            return;
        }

        // 检查下方支撑是否变化
        BlockPos downPos = pos.down();
        if (!StructureIntegrity.isSupportedBlock(world, downPos)) {
            // 延迟检测（确保新方块已放置）
            world.scheduleUpdate(pos, world.getBlockState(pos).getBlock(), 2);
        }
    }
}