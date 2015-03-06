package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

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
public class SayCmd extends RCommand {

    public SayCmd() {
        super("say");
        setRank(Rank.MOD);
        setPlayerOnly(true);
        setDescription("Broadcast a message to the whole server");
        setArgsUsage(" <Message>");
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        Bukkit.broadcastMessage("§5§l<§8Say§5§l> " + pl.getRankedName(false) + " §d" + StringUtils.join(args, " ", 0, args.length));
    }
}
