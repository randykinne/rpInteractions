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
public class TeleportCmd extends RCommand {

    public TeleportCmd() {
        super("teleport");
        setRank(Rank.MOD);
        setMinimumArgs(1);
        setMaximumArgs(1);
        setPlayerOnly(true);
        setAliases(Arrays.asList("tp"));
        setDescription("Teleport to player");
        setArgsUsage("<Player>");
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (Bukkit.getPlayer(args[0]) != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0]));
            pl.getPlayer().teleport(target.getPlayer().getLocation());
            pl.message("§6§l>> §eYou have teleported to " + target.getRankedName(false) + "§e.");
        }
    }
}
