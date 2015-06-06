package RandomPvP.Core.Server.Game.GameState;

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
public enum GameState {

    NONE("None", false),
    LOBBY("Lobby", true),
    STARTING("Starting", true),
    LOADING("Loading", true),
    WARMUP("Warmup", false),
    GRACE("Grace", false),
    BATTLE("Battle", true),
    BUILD("Build", true),
    RUNNING("Running", true),
    INGAME("InGame", true),
    DEATHMATCH("DeathMatch", false),
    ENDED("Ended", false);

    String name;
    boolean join;

    GameState(String name, boolean isJoinable) {
        this.name = name;
        this.join = isJoinable;
    }

    public String getName() {
        return name;
    }

    public boolean isJoinable() {
        return join;
    }
}
