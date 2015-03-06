package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
public class Giveaway extends RCommand {

    public Giveaway() {
        super("giveaway");
        setRank(Rank.ADMIN);
        setAliases(Arrays.asList("givearank", "winnerwinner"));
        setDescription("Someone gets lucky and wins Premium!");
        setArgsUsage("");
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        RPlayer winner = PlayerManager.getInstance().getRandomPlayer();

        while (winner.has(Rank.PREMIUM)) {
            winner = PlayerManager.getInstance().getRandomPlayer();
        }

        if (!winner.has(Rank.PREMIUM)) {
            winner.setRank(Rank.PREMIUM, true);
            winner.message(MsgType.INFO, "Congratulations! You were the lucky winner of the " + Rank.PREMIUM.getName() + " §7giveaway!");
            winner.message(MsgType.INFO, "Enjoy your free rank ;)");
            Bukkit.broadcastMessage("§8§l>> " + winner.getRankedName(false) + "§7 has won the giveaway!");
        }
    }
}
