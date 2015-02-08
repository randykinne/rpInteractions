package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.Broadcasts;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

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
public class ReportCmd extends RCommand {

    public ReportCmd() {
        super("report");
        setAliases(Arrays.asList("reoprt", "reprot", "rpt", "rprot", "reort", "reopt", "repot"));
        setPlayerOnly(true);
        setMinimumArgs(2);
        setDescription("Report a player to online staff");
        setArgsUsage("<Player> <Reason>");
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (Bukkit.getPlayer(args[0]) != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0]));

            pl.message(MsgType.CREDIT, "Your report has been sent to all online staff members.");
            pl.getPlayer().playSound(pl.getLocation(), Sound.ANVIL_BREAK, 1F, 1F);

            Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, "§f§l[R] §8(§a§l" + Bukkit.getServerName() + "§8) " + pl.getRankedName(false) + " §7> " + target.getRankedName(false) + "§8: §f" + getJoinedString(1));
            Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, "§f§l[R] §8[§a§l" + Bukkit.getServerName() + "§8) " + pl.getRankedName(false) + " §7> " + target.getRankedName(false) + "§8: §f" + getJoinedStrings2(1));
        } else {
            pl.message(MsgType.ERROR, "Player not found!");
        }
    }
}
