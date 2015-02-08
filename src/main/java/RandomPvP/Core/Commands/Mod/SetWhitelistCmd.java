package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Event.Server.RankWhitelistChangeEvent;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class SetWhitelistCmd extends RCommand {

    public SetWhitelistCmd() {
        super("setwhitelist");
        setRank(Rank.MOD);
        setDescription("Sets the minimum rank required to join");
        setArgsUsage("<Rank>");
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (Rank.valueOf(args[0].toUpperCase()) != null) {
            Rank rank = Rank.valueOf(args[0].toUpperCase());

            ServerToggles.setRankRequired(rank);
            Bukkit.getPluginManager().callEvent(new RankWhitelistChangeEvent(pl, rank));

        } else {
            List<String> names = new ArrayList<>();
            for (Rank rank : Rank.values()) {
                names.add(rank.getFormattedName() + ", ");
            }

            pl.message(MsgType.ERROR.getPrefix() + "Rank not found! Available: " + String.valueOf(Arrays.asList(names)).replace("[", "").replace("]", ""));
        }
    }
}
