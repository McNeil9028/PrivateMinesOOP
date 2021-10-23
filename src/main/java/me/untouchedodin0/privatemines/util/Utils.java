package me.untouchedodin0.privatemines.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.mines.Mine;
import me.untouchedodin0.privatemines.mines.MineType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.util.Objects;

public class Utils {

    private final PrivateMines privateMines;
    private final boolean debugMode;

    public Utils(PrivateMines privateMines) {
        this.privateMines = privateMines;
        this.debugMode = privateMines.isDebugMode();
    }

    public Location getRelative(Structure structure, int[] relative) {
        return structure
                .getRelative(relative[0], relative[1], relative[2])
                .getBlock()
                .getLocation();
    }

    public MineType getNextMineType(Mine mine) {
        MineType mineType = mine.getMineType();
        MineType upgradeMineType;
        boolean isAtLastMineType = privateMines.isAtLastMineType(mineType);
        if (isAtLastMineType) {
            privateMines.getLogger().info("Mine is already maxed out!");
            return mineType;
        }
        if (debugMode) {
            privateMines.getLogger().info("Current mine data Name: " + mineType.getName());
            upgradeMineType = privateMines.getNextMineType(mineType);
            privateMines.getLogger().info("Next mine data name: " + upgradeMineType.getName());
        }
        upgradeMineType = privateMines.getNextMineType(mineType);
        return upgradeMineType;
    }

    public double getPercentageLeft(Mine mine) {
        CuboidRegion cuboidRegion = mine.getCuboidRegion();
        int totalBlocks = cuboidRegion.getBlockVolume();
        long airBlocks = cuboidRegion.stream().filter(Block::isEmpty).count();
        return (double) airBlocks * 100 / totalBlocks;
    }

    @SuppressWarnings("unused")
    public CuboidRegion getRegion(Clipboard clipboard) {
        final BlockVector3 minimumPoint = clipboard.getRegion().getMinimumPoint();
        final BlockVector3 maximumPoint = clipboard.getRegion().getMaximumPoint();

        final int minX = minimumPoint.getBlockX();
        final int maxX = maximumPoint.getBlockX();
        final int minY = minimumPoint.getBlockY();
        final int maxY = maximumPoint.getBlockY();
        final int minZ = minimumPoint.getBlockZ();
        final int maxZ = maximumPoint.getBlockZ();

        World world;
        Location start;
        Location end;
        CuboidRegion cuboidRegion;

        if (clipboard.getRegion().getWorld() == null) {
            return null;
        }
        world = BukkitAdapter.adapt(Objects.requireNonNull(clipboard.getRegion().getWorld()));
        start = new Location(world, minX, minY, minZ);
        end = new Location(world, maxX, maxY, maxZ);
        cuboidRegion = new CuboidRegion(start, end);
        return cuboidRegion;
    }

    public Location blockVector3toBukkit(World world, BlockVector3 blockVector3) {
        Block block = world.getBlockAt(blockVector3.getBlockX(), blockVector3.getBlockY(), blockVector3.getBlockZ());
        return block.getLocation();
    }
}
