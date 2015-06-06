package RandomPvP.Core.Event.Game;

import RandomPvP.Core.Server.Game.Mode.Gamemode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
public class GamemodeChangeEvent extends Event {

    private Gamemode mode;
    private static HandlerList handlers = new HandlerList();

    public GamemodeChangeEvent(Gamemode mode) {
        this.mode = mode;
    }

    public Gamemode getMode() { return mode; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}
