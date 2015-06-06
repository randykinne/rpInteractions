package RandomPvP.Core.Event.Game;

import RandomPvP.Core.Server.Game.Map.RMap;
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
public class MapChangeEvent extends Event {

    private RMap map;
    private RMap oldMap;

    private static HandlerList handlers = new HandlerList();

    public MapChangeEvent(RMap newMap, RMap old) {
        newMap = map;
        oldMap = old;
    }

    public RMap getMap() {
        return map;
    }

    public RMap getOldMap() { return oldMap; }

    public String getName() {
        return map.getName();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}