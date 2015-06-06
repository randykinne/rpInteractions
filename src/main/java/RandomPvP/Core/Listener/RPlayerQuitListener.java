package RandomPvP.Core.Listener;

import RandomPvP.Core.Event.Player.RPlayerQuitEvent;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Scoreboard.ScoreboardManager;
import RandomPvP.Core.Server.Game.Team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        ScoreboardManager.getInstance().removePlayer(pl);
        if(pl != null) {
            Bukkit.getServer().getPluginManager().callEvent(new RPlayerQuitEvent(pl));

            if (pl.getTeam() != null) {
                TeamManager.leaveTeam(pl, pl.getTeam());
            }

            pl.saveData();

            if (pl.has(Rank.VIP)) {
                e.setQuitMessage(null); //proxy handles message
            } else {
                e.setQuitMessage("§c§l<< " + pl.getDisplayName(false) + " §9left.");
            }

            PlayerManager.getInstance().removePlayer(e.getPlayer());
        } else {
            e.setQuitMessage("§c§l<< §9" + e.getPlayer().getName() + " left.");
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        final RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
        ScoreboardManager.getInstance().removePlayer(pl);
        if(pl != null) {
            if (pl.getTeam() != null) {
                TeamManager.leaveTeam(pl, pl.getTeam());
            }

            pl.saveData();

            e.setLeaveMessage("§4§l<< " + pl.getDisplayName(false) + " §cwas kicked.");

            PlayerManager.getInstance().removePlayer(e.getPlayer());
        } else {
            e.setLeaveMessage("§4§l<< " + e.getPlayer().getName() + " §cwas kicked.");
        }
    }
}
