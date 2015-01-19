package RandomPvP.Core.Event.Game;

import RandomPvP.Core.Game.GameState.GameState;
import org.bukkit.event.Cancellable;
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
public class GameStateChangeEvent extends Event implements Cancellable {

    private GameState state;
    private boolean cancelled = false;
    private static HandlerList handlers = new HandlerList();

    public GameStateChangeEvent(GameState to) {
        state = to;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public String getName() {
        return state.getName();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
