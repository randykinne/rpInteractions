package RandomPvP.Core.Listener;

import RandomPvP.Core.Game.Team.TeamManager;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Broadcasts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
public class RPlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

            final RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());

            if (pl.getTeam() != null) {
                TeamManager.leaveTeam(pl, pl.getTeam());
            }

            new BukkitRunnable() {
                public void run() {
                    pl.saveData();
                }
            }.runTaskAsynchronously(RPICore.getInstance());


            if (pl.has(Rank.VIP)) {
                e.setQuitMessage(null);
                Broadcasts.sendRankedBroadcast(Rank.MOD, false, false, pl.getRankedName(false) + " §9left§7.");
            } else {
                e.setQuitMessage("§c§l<< " + pl.getRankedName(false) + " §9left.");
            }


            PlayerManager.getInstance().removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
        e.setLeaveMessage("§4§l<< " + pl.getRankedName(false) + " §cwas kicked.");
    }
}
