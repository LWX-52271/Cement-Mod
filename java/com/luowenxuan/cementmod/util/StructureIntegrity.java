package com.luowenxuan.cementmod.util;

import com.luowenxuan.cementmod.block.BlockRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class StructureIntegrity {
    // 可坍塌方块类型
    public static final Set<Block> STRUCTURAL_BLOCKS = new HashSet<Block>() {{
        add(BlockRegistryHandler.BLOCK_HARDENED_CONCRETE);
        add(BlockRegistryHandler.BLOCK_REINFORCED_CONCRETE);
    }};

    // 支撑方块类型
    private static final Set<Block> SUPPORT_BLOCKS = new HashSet<Block>() {{
        add(net.minecraft.init.Blocks.BEDROCK);
        add(net.minecraft.init.Blocks.OBSIDIAN);
        add(BlockRegistryHandler.BLOCK_STEEL_REBAR);
    }};

    // 最大检测范围
    private static final int MAX_SCAN_DISTANCE = 32;

    // 主检测方法
    public static void checkStructuralIntegrity(World world, BlockPos origin) {
        if (world.isRemote) return;

        // 步骤1: 获取所有连接的结构块
        Set<BlockPos> structureArea = scanConnectedStructure(world, origin);

        // 添加最小结构大小检查
        if (structureArea.size() < 2) {
            return; // 单个方块不需要结构检查
        }

        // 步骤2: 检测支撑状态
        Map<BlockPos, Boolean> supportMap = checkSupportStatus(world, structureArea);

        // 步骤3: 分离无支撑区域
        Set<BlockPos> unstableArea = new HashSet<>();
        for (BlockPos pos : structureArea) {
            if (!supportMap.getOrDefault(pos, false)) {
                unstableArea.add(pos);
            }
        }

        // 步骤4: 执行坍塌
        if (!unstableArea.isEmpty()) {
            scheduleCollapse(world, unstableArea);
        }
    }

    // 洪水填充算法扫描连接结构
    private static Set<BlockPos> scanConnectedStructure(World world, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            visited.add(current);

            // 距离限制
            if (start.distanceSq(current) > MAX_SCAN_DISTANCE * MAX_SCAN_DISTANCE) continue;

            // 检查六个方向
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighbor = current.offset(facing);

                // 跳过已访问或非结构块
                if (visited.contains(neighbor)) continue;
                if (!isStructuralBlock(world, neighbor)) continue;

                queue.add(neighbor);
            }
        }
        return visited;
    }

    // 检测支撑状态（从下到上）
    private static Map<BlockPos, Boolean> checkSupportStatus(World world, Set<BlockPos> blocks) {
        // 按Y坐标排序（从低到高）
        List<BlockPos> sortedBlocks = new ArrayList<>(blocks);
        sortedBlocks.sort(Comparator.comparingInt(BlockPos::getY));

        Map<BlockPos, Boolean> supportMap = new HashMap<>();

        for (BlockPos pos : sortedBlocks) {
            boolean isSupported = false;

            // 检查1: 是否在地面层
            if (pos.getY() <= world.getSeaLevel() - 5) {
                isSupported = true;
            }
            // 检查2: 下方有支撑块
            else if (isSupportedBlock(world, pos.down())) {
                isSupported = true;
            }
            // 检查3: 相邻有支撑块
            else {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    BlockPos sidePos = pos.offset(facing);
                    if (supportMap.getOrDefault(sidePos, false) &&
                            world.getBlockState(sidePos).isSideSolid(world, sidePos, facing.getOpposite())) {
                        isSupported = true;
                        break;
                    }
                }
            }

            // 检查4: 钢筋加固
            if (!isSupported && world.getBlockState(pos).getBlock() == BlockRegistryHandler.BLOCK_REINFORCED_CONCRETE) {
                isSupported = isReinforced(world, pos);
            }

            supportMap.put(pos, isSupported);
        }

        return supportMap;
    }

    // 检查位置是否被钢筋加固
    private static boolean isReinforced(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos checkPos = pos.offset(facing);
            IBlockState neighbor = world.getBlockState(checkPos);
            if (neighbor.getBlock() == BlockRegistryHandler.BLOCK_STEEL_REBAR) {
                return true;
            }
        }
        return false;
    }

    // 安排坍塌顺序
    private static void scheduleCollapse(World world, Set<BlockPos> unstableBlocks) {
        // 按Y坐标分组
        Map<Integer, List<BlockPos>> heightGroups = new TreeMap<>(Collections.reverseOrder());
        for (BlockPos pos : unstableBlocks) {
            heightGroups.computeIfAbsent(pos.getY(), k -> new ArrayList<>()).add(pos);
        }

        // 分批坍塌（每层间隔5刻）
        int delay = 0;
        for (List<BlockPos> layer : heightGroups.values()) {
            for (BlockPos pos : layer) {
                IBlockState state = world.getBlockState(pos);
                world.scheduleUpdate(pos, state.getBlock(), delay);
            }
            delay += 5;
        }
    }

    // 判断是否为结构块
    private static boolean isStructuralBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return STRUCTURAL_BLOCKS.contains(state.getBlock());
    }

    // 判断是否为支撑块（修复支撑检测逻辑）
    public static boolean isSupportedBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        // 所有固体方块都视为支撑
        if (state.isSideSolid(world, pos, EnumFacing.UP)) {
            return true;
        }

        // 特定支撑方块
        return SUPPORT_BLOCKS.contains(state.getBlock());
    }
}