package com.tonywww.dustandash.block.entity.FissionReactor;

import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

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
        Block casing = DAABlocks.FISSION_REACTOR_CASING.get();
        int radius = findRadius(level, controllerPos, casing, maxRadius);
        if (radius == 0) {
            return Optional.empty();
        }

        int height = findHeight(level, controllerPos, casing, radius, maxHeight);
        if (height == 0 || !hasValidShell(level, controllerPos, casing, radius, height)) {
            return Optional.empty();
        }

        return Optional.of(new ReactorStructureSnapshot(radius, height,
                findInterface(level, controllerPos, radius, height)));
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
        BlockPos.MutableBlockPos cursor = controllerPos.north(radius).below().mutable();
        for (int height = 2; height <= maxHeight; height++) {
            cursor.move(Direction.DOWN);
            if (level.getBlockState(cursor).is(casing)) {
                return height;
            }
        }
        return 0;
    }

    private static boolean hasValidShell(Level level, BlockPos controllerPos, Block casing,
                                         int radius, int height) {
        BlockPos firstCorner = controllerPos.below().north(radius).east(radius);
        BlockPos secondCorner = controllerPos.below(height).south(radius).west(radius);
        if (!level.getBlockState(firstCorner).is(casing) || !level.getBlockState(secondCorner).is(casing)) {
            return false;
        }

        BlockPos firstWest = firstCorner;
        BlockPos firstSouth = firstCorner;
        BlockPos firstDown = firstCorner;
        BlockPos secondNorth = secondCorner;
        BlockPos secondEast = secondCorner;
        BlockPos secondUp = secondCorner;
        for (int offset = 0; offset < radius * 2; offset++) {
            firstWest = firstWest.west();
            firstSouth = firstSouth.south();
            firstDown = firstDown.below();
            secondNorth = secondNorth.north();
            secondEast = secondEast.east();
            secondUp = secondUp.above();
            if (!level.getBlockState(firstWest).is(casing)
                    || !level.getBlockState(firstSouth).is(casing)
                    || !level.getBlockState(firstDown).is(casing)
                    || !level.getBlockState(secondNorth).is(casing)
                    || !level.getBlockState(secondEast).is(casing)
                    || !level.getBlockState(secondUp).is(casing)) {
                return false;
            }
        }

        for (int firstOffset = 0; firstOffset < radius * 2 - 1; firstOffset++) {
            for (int secondOffset = 0; secondOffset < radius * 2 - 1; secondOffset++) {
                if (!isWall(level, new BlockPos(controllerPos.getX() - radius + 1 + firstOffset,
                                controllerPos.getY() - 1, controllerPos.getZ() - radius + 1 + secondOffset))
                        || !isWall(level, new BlockPos(controllerPos.getX() - radius + 1 + firstOffset,
                                controllerPos.getY() - height, controllerPos.getZ() - radius + 1 + secondOffset))
                        || !isWall(level, new BlockPos(controllerPos.getX() - radius,
                                controllerPos.getY() - height + 1 + secondOffset,
                                controllerPos.getZ() - radius + 1 + firstOffset))
                        || !isWall(level, new BlockPos(controllerPos.getX() + radius,
                                controllerPos.getY() - height + 1 + secondOffset,
                                controllerPos.getZ() - radius + 1 + firstOffset))
                        || !isWall(level, new BlockPos(controllerPos.getX() - radius + 1 + firstOffset,
                                controllerPos.getY() - height + 1 + secondOffset, controllerPos.getZ() - radius))
                        || !isWall(level, new BlockPos(controllerPos.getX() - radius + 1 + firstOffset,
                                controllerPos.getY() - height + 1 + secondOffset, controllerPos.getZ() + radius))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isWall(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModTags.Blocks.FISSION_REACTOR_WALL);
    }

    private static BlockPos findInterface(Level level, BlockPos controllerPos, int radius, int height) {
        BlockPos center = controllerPos.below((height / 2) + 1);
        for (Direction direction : HORIZONTAL_SCAN_ORDER) {
            BlockPos candidate = center.relative(direction, radius + 1);
            if (level.getBlockEntity(candidate) instanceof FissionReactorInterfaceEntity) {
                return candidate;
            }
        }
        return null;
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