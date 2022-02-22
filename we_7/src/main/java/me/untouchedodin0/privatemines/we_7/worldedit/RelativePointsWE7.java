package me.untouchedodin0.privatemines.we_7.worldedit;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.bukkit.util.BlockVector;

public class RelativePointsWE7 {

    World world;
    BlockVector3 spawn;
    BlockVector3 corner1;
    BlockVector3 corner2;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockVector3 getSpawn() {
        return spawn;
    }

    public void setSpawn(BlockVector3 spawn) {
        this.spawn = spawn;
    }

    public BlockVector3 getCorner1() {
        return corner1;
    }

    public void setCorner1(BlockVector3 corner1) {
        this.corner1 = corner1;
    }

    public BlockVector3 getCorner2() {
        return corner2;
    }

    public void setCorner2(BlockVector3 corner2) {
        this.corner2 = corner2;
    }
}
