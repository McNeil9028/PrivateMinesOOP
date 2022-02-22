package me.untouchedodin0.privatemines.we_7.worldedit;

import com.fastasyncworldedit.core.Fawe;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import me.untouchedodin0.privatemines.compat.WorldEditAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.BlockVector;
import redempt.redlib.region.CuboidRegion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WE7Adapter implements WorldEditAdapter {

    BlockVector3 spawnPoint;
    List<BlockVector3> corners = new ArrayList<>(2);

    @Override
    public CuboidRegion pasteSchematic(Location location, Path file) {

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file.toFile());
        World world = BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld()));
        EditSession editSessionFAWE = Fawe.instance().getWorldEdit().newEditSession(world);

        if (clipboardFormat == null) {
            throw new IllegalArgumentException("File is not a valid schematic");
        }
        try (InputStream fix = Files.newInputStream(file); ClipboardReader clipboardReader =
                clipboardFormat.getReader(fix)) {
            Clipboard clipboard = clipboardReader.read();

            if (clipboard == null) {
                throw new IllegalArgumentException("Clipboard is null");
            }

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                BlockVector3 centerVector = BlockVector3.at(location.getX(), location.getY(), location.getZ());

                // If the clipboard isn't null prepare to create a paste operation, complete it and set the region stuff.
                Operation operationFAWE = new ClipboardHolder(clipboard)
                        .createPaste(editSessionFAWE)
                        .to(centerVector)
                        .ignoreAirBlocks(true)
                        .build();
                    Operations.complete(operationFAWE);

                Region region = clipboard.getRegion();
                region.shift(centerVector.subtract(clipboard.getOrigin()));
                return new CuboidRegion(BukkitAdapter.adapt(location.getWorld(), region.getMinimumPoint()), BukkitAdapter.adapt(location.getWorld(), region.getMaximumPoint()));
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void fillRegion(CuboidRegion region, Map<Material, Double> materials) {
        World world = new BukkitWorld(region.getWorld());
        EditSession editSessionFAWE  = Fawe.instance().getWorldEdit().newEditSession(world);

        try (final EditSession editSession =
                     WorldEdit.getInstance().newEditSession(world)) {
            editSession.setReorderMode(EditSession.ReorderMode.MULTI_STAGE);
            final RandomPattern randomPattern = new RandomPattern();

            materials.forEach((material, chance) -> {
                Pattern pattern = BukkitAdapter.adapt(material.createBlockData()).toBaseBlock();
                randomPattern.add(pattern, chance);
            });

            final com.sk89q.worldedit.regions.CuboidRegion worldEditRegion = new com.sk89q.worldedit.regions.CuboidRegion(
                    BukkitAdapter.asBlockVector(region.getStart()),
                    BukkitAdapter.asBlockVector(region.getEnd())
            );

            editSessionFAWE.setBlocks((Region) worldEditRegion, randomPattern);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            // this shouldn't happen
        }
    }

    public BlockVector3 findRelativeSpawnPoint(Region region, Material spawnMaterial) {
        Utils utils = new Utils();
        World world = region.getWorld();
        region.forEach(blockVector3 -> {
            if (utils.getType(world, blockVector3).equals(spawnMaterial)) {
                spawnPoint = blockVector3;
            }
        });
        return spawnPoint;
    }

    public List<BlockVector3> findCornerPoints(Region region, Material cornerMaterial) {
        Utils utils = new Utils();
        World world = region.getWorld();

        region.forEach(blockVector3 -> {
            if (utils.getType(world, blockVector3).equals(cornerMaterial)) {
                corners.add(blockVector3);
            }
        });
        return corners;
    }
}
