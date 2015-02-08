package RandomPvP.Core.Game;

import RandomPvP.Core.RPICore;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class GameEffects {

    public void regenerateBlocksAtOnce(final List<Block> blocks, int delay) {
        if (delay == 0) {
            delay = 40;
        }

        for (Block b : blocks) {
            final BlockState state = b.getState();

            b.setType(Material.AIR);

            if ((b.getType() == Material.SAND) || (b.getType() == Material.GRAVEL)) {
                delay += 1;
            }

            new BukkitRunnable() {
                public void run() {
                    state.update(true, false);
                }
            }.runTaskLater(RPICore.getInstance(), delay);
        }
    }

    public void regenFromUtil(final List<BlockState> blocks, final boolean effect, final int speed) {

        new BukkitRunnable() {
            int i = -1;
            @SuppressWarnings("deprecation")
            public void run() {
                if (i != blocks.size()-1) {
                    i++;
                    BlockState bs = blocks.get(i);
                    bs.getBlock().setType(bs.getType());
                    bs.getBlock().setData(bs.getBlock().getData());
                    if (effect)
                        bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
                }else{
                    for (BlockState bs : blocks) {
                        bs.getBlock().setType(bs.getType());
                        bs.getBlock().setData(bs.getBlock().getData());
                    }
                    blocks.clear();
                    this.cancel();
                }
            }
        }.runTaskTimer(RPICore.getInstance(), speed, speed);
    }

    public static void performWorldRegen(final List<BlockState> blocks, final Location center, final int blocksPerTime, long delay){
        new BukkitRunnable(){
            @Override
            public void run(){
                regenerateBlocks(blocks, blocksPerTime, 12, new Comparator<BlockState>(){
                    @Override
                    public int compare(BlockState state1, BlockState state2){
                        return Double.compare(state1.getLocation().distance(center), state2.getLocation().distance(center));
                    }
                });
            }
        }.runTaskLater(RPICore.getInstance(), delay);
    }

    public static void regenerateBlocks(Collection<BlockState> blocks, final int blocksPerTime, final long delay, Comparator<BlockState> comparator){
        final List<BlockState> orderedBlocks = new ArrayList<>();

        orderedBlocks.addAll(blocks);

        if(comparator != null){
            Collections.sort(orderedBlocks, comparator);
        }

        final int size = orderedBlocks.size();

        if(size > 0){
            new BukkitRunnable(){
                int index = size - 1;

                @Override
                public void run(){
                    for(int i = 0; i < blocksPerTime; i++){
                        if(index >= 0){
                            final BlockState state = orderedBlocks.get(index);

                            regenerateBlock(state.getBlock(), state.getType(), state.getData().getData());

                            index -= 1;
                        } else {
                            this.cancel();
                            return;
                        }
                    }
                }
            }.runTaskTimer(RPICore.getInstance(), 0L, delay);
        }
    }

    public static void regenerateBlock(Block block, final Material type, final byte data){
        final Location loc = block.getLocation();

        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, (type == Material.AIR ? block.getType().getId() : type.getId()));
        block.setType(type);
        block.setData(data);
    }
}
