package RandomPvP.Core.Game.Team.Exception;

import RandomPvP.Core.Player.RPlayer;

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
public class TeamException extends Exception {

    private String message;

    public TeamException(RPlayer pl, String message1) {
        message = message1;
        pl.message("§3§l>> §7" + getMessage());
    }

    public String getMessage() {
        return message;
    }
}
