package RandomPvP.Core.Commands.VIP;

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
public class GoToCmd extends RCommand {

    public GoToCmd() {
        super("goto");
        setPlayerOnly(true);
        setRank(Rank.VIP);
        setAliases(Arrays.asList("vipgo", "vipgoto"));
        setDescription("Takes you where you want to go!");
        setArgsUsage("<Server>");
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer player, String string, String[] args) {
        String server = "H1";

        switch (args[0]) {
            case "PvPAcademy": server = "PA1"; break;
            case "pvpacademy": server = "PA1"; break;
            case "pvpac": server = "PA1"; break;
            case "PA": server = "PA1"; break;
            case "PA1": server = "PA1"; break;

            case "UltraHardCore": server = "UHC1"; break;
            case "UHC": server = "UHC1"; break;

            case "GhostCraft": server = "GC1"; break;
            case "GC": server = "GC1"; break;
            case "Ghost": server = "GC1"; break;
            case "gh": server = "GC1"; break;
            case "gho": server = "GC1"; break;

            default:
        }

        player .send(server);

    }

}
