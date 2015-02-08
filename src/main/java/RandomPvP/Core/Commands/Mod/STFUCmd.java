package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;

import java.util.Arrays;

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
public class STFUCmd extends RCommand {

    public STFUCmd() {
        super("stfu");
        setRank(Rank.MOD);
        setPlayerOnly(true);
        setDescription("Toggles staff chat notifications");
        setAliases(Arrays.asList("togglestaffmessages", "togglestfu"));
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        pl.getStaff().toggleSTFU();
    }
}
