package RandomPvP.Core.Listener;

import RandomPvP.Core.Event.Game.GameStateChangeEvent;
import RandomPvP.Core.Event.Game.GamemodeChangeEvent;
import RandomPvP.Core.Event.Game.MapChangeEvent;
import RandomPvP.Core.Event.Server.RankWhitelistChangeEvent;
import RandomPvP.Core.Game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
public class ServerUpdateListener implements Listener {

    @EventHandler
    public void onChange(GameStateChangeEvent e) {
        if (!e.isCancelled()) {
            GameManager.updateGame();
            System.out.println("GameState changed to " + e.getState().getName());
        }
    }

    @EventHandler
    public void onChange(GamemodeChangeEvent e) {
        GameManager.updateGame();
        System.out.println("Game Mode changed to " + e.getMode().getName());
    }

    @EventHandler
    public void onChange(MapChangeEvent e) {
        GameManager.updateGame();
        System.out.println("Game Map changed to " + e.getMap().getName());
    }

    @EventHandler
    public void onChange(RankWhitelistChangeEvent e) {
        if (!e.isCancelled()) {
            GameManager.updateGame();
            System.out.println("Game Rank Whitelist changed to " + e.getRank().getName());
        }
    }
}
