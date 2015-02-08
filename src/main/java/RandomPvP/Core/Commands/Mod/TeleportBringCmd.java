package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.Bukkit;

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
public class TeleportBringCmd extends RCommand {

    public TeleportBringCmd() {
        super("bring");
        setRank(Rank.MOD);
        setMaximumArgs(1);
        setDescription("Teleport a player to your location");
        setArgsUsage("<Player>");
        setPlayerOnly(true);
        setAliases(Arrays.asList("tphere"));
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (Bukkit.getPlayer(args[0]) != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0]));
            target.getPlayer().teleport(pl.getPlayer().getLocation());
            pl.message("§6§l>> §eYou have teleported " + target.getRankedName(false) + " §eto you.");
            target.message("§6§l>> §eYou have been teleported to " + pl.getRankedName(false) + "§e.");
        }
    }
}
