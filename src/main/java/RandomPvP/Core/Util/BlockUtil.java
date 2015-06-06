package RandomPvP.Core.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class BlockUtil {

    public void setBlocks(Location min, Location max, String world, Material mat) {
        int minx = min.getBlockX();
        int miny = min.getBlockY();
        int minz = min.getBlockZ();
        int maxx = max.getBlockX();
        int maxy = max.getBlockY();
        int maxz = max.getBlockZ();
        for (int x = minx; x <= maxx; x++) {
            for (int y = miny; y <= maxy; y++) {
                for (int z = minz; z <= maxz; z++) {
                    Bukkit.getWorld(world).getBlockAt(new Location(Bukkit.getWorld(world), x, y, z)).setType(mat);
                }
            }
        }
    }

    public Collection<Location> getLocationsBetween(Location min, Location max, String world) {
        Collection<Location> collection = new ArrayList<>();
        {
            int minx = min.getBlockX();
            int miny = min.getBlockY();
            int minz = min.getBlockZ();
            int maxx = max.getBlockX();
            int maxy = max.getBlockY();
            int maxz = max.getBlockZ();
            for (int x = minx; x <= maxx; x++) {
                for (int y = miny; y <= maxy; y++) {
                    for (int z = minz; z <= maxz; z++) {
                        collection.add(new Location(Bukkit.getWorld(world), x, y, z));
                    }
                }
            }
        }
        return collection;
    }

    public Collection<BlockState> getAsStates(Collection<Block> blocks) {
        return getAsStates(blocks, null);
    }

    public Collection<BlockState> getAsStates(Collection<Block> blocks, List<Material> remove) {
        Collection<BlockState> states = new ArrayList<>();
        {
            for(Block b : blocks) {
                if(remove != null && !remove.contains(b.getType())) {
                    continue;
                }

                states.add(b.getState());
            }
        }
        return states;
    }

}
