package RandomPvP.Core.Game.Executor;

import org.bukkit.scheduler.BukkitRunnable;

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
public class GameTick extends BukkitRunnable {

    public int ticks = 0;

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    boolean hasEnded() {
        return getTicks() <= 0;
    }

    @Override
    public void run() {
        if (!hasEnded()) {
            setTicks(getTicks() - 1);
        }
    }
}
