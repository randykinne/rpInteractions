package RandomPvP.Core.Game;

import RandomPvP.Core.Event.Game.GameStateChangeEvent;
import RandomPvP.Core.Event.Game.GamemodeChangeEvent;
import RandomPvP.Core.Game.GameState.GameState;
import RandomPvP.Core.Game.Map.RMap;
import RandomPvP.Core.Game.Mode.Gamemode;
import org.bukkit.Bukkit;

import java.util.ArrayList;

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
public class GameManager {

    static Game game;
    static GameState state;
    static Gamemode mode;
    static RMap map;

    public static void setGame(Game gam) { game = gam;}
    public static Game getGame() {
        return game;
    }

    public static RMap getMap() { return map; }
    public static void setMap(RMap world) { map = world; }

    public static void setMode(Gamemode m) {
        if (m != mode) {
            mode = m;
            Bukkit.getPluginManager().callEvent(new GamemodeChangeEvent(m));
        }
    }
    public static Gamemode getMode() { return mode; }

    public static void setState(GameState state1) {
        if (state != state1) {
            state = state1;
            Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(state1));
        }

    }
    public static GameState getState() {
        return state;
    }

    public boolean isState(GameState compare) {
        return state == compare;
    }
}
