package RandomPvP.Core.Event.Player;

import RandomPvP.Core.Player.RPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RPlayerQuitEvent extends Event {

    private RPlayer pl;
    private static final HandlerList handlers = new HandlerList();

    public RPlayerQuitEvent(RPlayer pl) {
        this.pl = pl;
    }

    public RPlayer getPlayer() {
        return pl;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
