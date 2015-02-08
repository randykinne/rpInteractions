package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;

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
public class TestCommand extends RCommand {

    public TestCommand() {
        super("test");
        setPlayerOnly(true);
        setDescription("Testing!");
        setArgsUsage("<SubCommand>");
        setRank(Rank.ADMIN);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        pl.message(getName());
    }
}
