package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class RankCmd extends RCommand {

    public RankCmd() {
        super("rank");
        setRank(Rank.ADMIN);
        setDescription("Set a player's rank");
        setArgsUsage("<Player> <Rank>");
        setMinimumArgs(2);
        setMaximumArgs(2);
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        Rank toSet;
        {
            try {
                toSet = Rank.valueOf(args[1].toUpperCase());
            } catch (Exception ex) {
                pl.message(MsgType.ERROR, "Please supply a valid rank. Ranks are " + Rank.values().toString().replace("[", "").replace("]", ""));
                return;
            }
        }

        if(PlayerManager.getInstance().getPlayer(args[0]) != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(args[0]);
            if(target.getRank() != toSet) {
                target.setRank(toSet, true);
                target.message(MsgType.INFO, "Your rank has been set to " + toSet.getTag() + ChatColor.GRAY + ".");

                pl.message(MsgType.INFO, "Successfully set " + args[0] + " to " + toSet.getTag() + ChatColor.GRAY + ".");
            } else {
                pl.message(MsgType.ERROR, target.getRankedName(false) + ChatColor.GRAY + " already has " + toSet.getTag() + ChatColor.GRAY + ".");
            }
        } else {
            OfflineRPlayer player = new OfflineRPlayer(args[0]);
            {
                player.setRank(toSet);
            }
            player.saveData();

            pl.message(MsgType.INFO, "Successfully set " + args[0] + " to " + toSet.getTag() + ChatColor.GRAY + ".");
        }
    }
}
