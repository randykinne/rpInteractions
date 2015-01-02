package RandomPvP.Core.Game.GameState;

import RandomPvP.Core.Game.Game;
import RandomPvP.Core.Game.GameManager;

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
public class GameState {

    String state;
    int stateId;
    Game gameFor;
    boolean joinable;

    public GameState(String state, int id, Game type) {
        this.state = state;
        this.stateId = id;
        this.gameFor = type;
    }

    public boolean isJoinable() {
        return joinable;
    }
    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public int getID() {
        return stateId;
    }

    public String getName() {
        return state;
    }

    public void registerGameState(GameState state) {
            GameManager.gameStates.add(state);
    }
}
