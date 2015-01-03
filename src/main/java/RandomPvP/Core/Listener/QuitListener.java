package RandomPvP.Core.Listener;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Game.Team.TeamManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Util.RPStaff;
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
public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer(e.getPlayer());

            if (pl.getTeam() != null) {
                TeamManager.leaveTeam(pl, pl.getTeam());
            }

            pl.saveData();

            if (pl.isVIP()) {
                e.setQuitMessage(null);
                RPStaff.sendVIPMessage(pl.getRankedName(false) + " §7left " + GameManager.getGame().getPrimaryColor() + Bukkit.getServerName() + "§7.", true);
            } else {
                e.setQuitMessage("§4§l<< " + pl.getRankedName(false));
            }


            RPlayerManager.getInstance().removePlayer(e.getPlayer());
    }

    /*@EventHandler
    public void onKick(PlayerKickEvent e) {
        RPlayer pl = RPlayerManager.getInstance().getPlayer(e.getPlayer());

        pl.saveData();

        if (pl.isStaff() || pl.isVIP()) {
            e.setLeaveMessage(null);
            RPStaff.sendVIPMessage(pl.getRankedName(false) + " §7was kicked from " + GameManager.getGame().getPrimaryColor() + Bukkit.getServerName() + "§7.", true);
        } else {
            e.setLeaveMessage("§4§l<< " + pl.getRankedName(false) + " §7was kicked.");
        }


        RPlayerManager.getInstance().removePlayer(e.getPlayer());


    }*/
}
