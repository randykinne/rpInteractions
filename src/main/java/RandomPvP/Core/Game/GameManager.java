package RandomPvP.Core.Game;

import RandomPvP.Core.Game.GameState.GameState;

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

    public static ArrayList<GameState> gameStates = new ArrayList<GameState>();

    public static void setGame(Game gam) { game = gam;}
    public static Game getGame() {
        return game;
    }

    public static void setState(GameState state1) {
        state = state1;
    }
    public static GameState getState() {
        return state;
    }

}
