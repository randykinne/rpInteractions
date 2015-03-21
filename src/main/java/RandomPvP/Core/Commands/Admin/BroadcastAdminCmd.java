package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.NetworkUtil;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
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
public class BroadcastAdminCmd extends RCommand {

    public BroadcastAdminCmd() {
        super("broadcast");
        setRank(Rank.ADMIN);
        setPlayerOnly(true);
        setAliases(Arrays.asList("bc"));
        setDescription("Broadcasts message to all players");
        setArgsUsage("<Message>");
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        Broadcasts.sendRankedBroadcast(Rank.PLAYER, false, true, "\n  " + pl.getRankedName(false) + " §esays... \n  §7" + StringUtils.join(args, " ") + "\n ");
    }
}
