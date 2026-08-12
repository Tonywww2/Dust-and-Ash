package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ReactorStructureScanner {
    private static final Direction[] NEIGHBORS = Direction.values();
    private static final Direction[] HORIZONTAL_SCAN_ORDER = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };

    private ReactorStructureScanner() {
    }

    public static Optional<ReactorStructureSnapshot> scan(Level level, BlockPos controllerPos,
                                                           int maxRadius, int maxHeight) {
        return diagnose(level, controllerPos, maxRadius, maxHeight).structureOptional();
    }

    public static ReactorStructureDiagnostic diagnose(Level level, BlockPos controllerPos,
                                                       int maxRadius, int maxHeight) {
        Block casing = DAABlocks.FISSION_REACTOR_CASING.get();
        int radius = findRadius(level, controllerPos, casing, maxRadius);
        if (radius == 0) {
            return ReactorStructureDiagnostic.invalid(ReactorStructureIssue.RADIUS_ANCHOR_MISSING);
        }

        int height = findHeight(level, controllerPos, casing, radius, maxHeight);
        if (height == 0) {
            return ReactorStructureDiagnostic.invalid(ReactorStructureIssue.HEIGHT_ANCHOR_MISSING);
        }

        BlockPos invalidCasing = findInvalidCasing(level, controllerPos, casing, radius, height);
        if (invalidCasing != null) {
            return ReactorStructureDiagnostic.invalid(ReactorStructureIssue.INVALID_CASING, invalidCasing);
        }

        BlockPos invalidWall = findInvalidWall(level, controllerPos, radius, height);
        if (invalidWall != null) {
            return ReactorStructureDiagnostic.invalid(ReactorStructureIssue.INVALID_WALL, invalidWall);
        }

        return ReactorStructureDiagnostic.formed(new ReactorStructureSnapshot(
                radius,
                height,
                findInterface(level, controllerPos, radius, height)
        ));
    }

    public static ReactorCoreSnapshot scanCore(Level level, BlockPos controllerPos,
                                                ReactorStructureSnapshot structure) {
        Block fuelCell = DAABlocks.FISSION_REACTOR_FUEL_CELL.get();
        Block coolingCell = DAABlocks.FISSION_REACTOR_COOLING_CELL.get();
        List<ReactorCoreSnapshot.FuelCellEnvironment> fuelCells = new ArrayList<>();
        int coolingCellCount = 0;

        for (int depth = 2; depth < structure.height(); depth++) {
            for (int xOffset = 0; xOffset < structure.radius() * 2 - 1; xOffset++) {
                for (int zOffset = 0; zOffset < structure.radius() * 2 - 1; zOffset++) {
                    BlockPos cellPos = new BlockPos(
                            controllerPos.getX() - structure.radius() + 1 + xOffset,
                            controllerPos.getY() - depth,
                            controllerPos.getZ() - structure.radius() + 1 + zOffset
                    );
                    Block cell = level.getBlockState(cellPos).getBlock();
                    if (cell == fuelCell) {
                        fuelCells.add(scanFuelCellEnvironment(level, cellPos, fuelCell, coolingCell));
                    } else if (cell == coolingCell) {
                        coolingCellCount++;
                    }
                }
            }
        }

        return new ReactorCoreSnapshot(fuelCells, coolingCellCount);
    }

    private static int findRadius(Level level, BlockPos controllerPos, Block casing, int maxRadius) {
        BlockPos.MutableBlockPos cursor = controllerPos.below().mutable();
        for (int radius = 1; radius <= maxRadius; radius++) {
            cursor.move(Direction.NORTH);
            if (level.getBlockState(cursor).is(casing)) {
                return radius;
            }
        }
        return 0;
    }

    private static int findHeight(Level level, BlockPos controllerPos, Block casing, int radius, int maxHeight) {
        int minimumHeight = radius * 2 + 1;
        for (int height = minimumHeight; height <= maxHeight; height++) {
            BlockPos candidate = controllerPos.north(radius).below(height);
            if (level.getBlockState(candidate).is(casing)) {
                return height;
            }
        }
        return 0;
    }

    @Nullable
    private static BlockPos findInvalidCasing(Level level, BlockPos controllerPos, Block casing,
                                              int radius, int height) {
        for (int depth = 1; depth <= height; depth++) {
            for (int xOffset = -radius; xOffset <= radius; xOffset++) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    if (boundaryAxisCount(xOffset, depth, zOffset, radius, height) < 2) {
                        continue;
                    }
                    BlockPos edgePos = controllerPos.offset(xOffset, -depth, zOffset);
                    if (!level.getBlockState(edgePos).is(casing)) {
                        return edgePos;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private static BlockPos findInvalidWall(Level level, BlockPos controllerPos, int radius, int height) {
        for (int depth = 1; depth <= height; depth++) {
            for (int xOffset = -radius; xOffset <= radius; xOffset++) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    if (boundaryAxisCount(xOffset, depth, zOffset, radius, height) != 1) {
                        continue;
                    }
                    BlockPos wallPos = controllerPos.offset(xOffset, -depth, zOffset);
                    if (!isWall(level, wallPos)) {
                        return wallPos;
                    }
                }
            }
        }
        return null;
    }

    private static int boundaryAxisCount(int xOffset, int depth, int zOffset, int radius, int height) {
        int count = Math.abs(xOffset) == radius ? 1 : 0;
        count += depth == 1 || depth == height ? 1 : 0;
        count += Math.abs(zOffset) == radius ? 1 : 0;
        return count;
    }

    private static boolean isWall(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.FISSION_REACTOR_WALL);
    }

    private static BlockPos findInterface(Level level, BlockPos controllerPos, int radius, int height) {
        BlockPos center = getInterfaceCenter(controllerPos, height);
        for (Direction direction : HORIZONTAL_SCAN_ORDER) {
            BlockPos candidate = center.relative(direction, radius + 1);
            if (level.getBlockEntity(candidate) instanceof FissionReactorInterfaceEntity) {
                return candidate;
            }
        }
        return null;
    }

    public static BlockPos getSuggestedInterfacePos(BlockPos controllerPos, int radius, int height) {
        return getInterfaceCenter(controllerPos, height).north(radius + 1);
    }

    private static BlockPos getInterfaceCenter(BlockPos controllerPos, int height) {
        return controllerPos.below((height / 2) + 1);
    }

    private static ReactorCoreSnapshot.FuelCellEnvironment scanFuelCellEnvironment(
            Level level, BlockPos cellPos, Block fuelCell, Block coolingCell) {
        int surroundingFuelCells = 0;
        int surroundingCoolingCells = 0;
        for (Direction direction : NEIGHBORS) {
            Block neighbor = level.getBlockState(cellPos.relative(direction)).getBlock();
            if (neighbor == fuelCell) {
                surroundingFuelCells++;
            }
            if (neighbor == coolingCell) {
                surroundingCoolingCells++;
            }
        }
        return new ReactorCoreSnapshot.FuelCellEnvironment(surroundingFuelCells, surroundingCoolingCells);
    }
}