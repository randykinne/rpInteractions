package RandomPvP.Core.Util.Runnable;

import RandomPvP.Core.RPICore;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract class TimedTask {

    public TimedTask(final int time, final int[] toCall, final int[] moludo) {
        new BukkitRunnable() {
            int t = time;
            public void run() {
                if(t != 0) {
                    for (int i : toCall) {
                        if(t == i) {
                            call();
                            break;
                        }
                    }
                    for (int i : moludo) {
                        if(t % i == 0) {
                            call();
                            break;
                        }
                    }
                } else {
                    end();
                    cancel();
                }
            }
        }.runTaskTimer(RPICore.getInstance(), 20L, 20L);
    }

    public TimedTask(final int time) {
        new BukkitRunnable() {
            int t = time;
            public void run() {
                if(t != 0) {
                    call();
                } else {
                    end();
                    cancel();
                }
            }
        }.runTaskTimer(RPICore.getInstance(), 20L, 20L);
    }

    public abstract void call();

    public abstract void end();

}
