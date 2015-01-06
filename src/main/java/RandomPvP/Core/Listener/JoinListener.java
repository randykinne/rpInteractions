package RandomPvP.Core.Listener;


import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.*;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

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
public class JoinListener implements Listener {

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent e) {
        new BukkitRunnable() {
            public void run() {
                boolean allowed = true;
                UUID id = e.getUniqueId();

                if (id != null) {
                        if (ServerToggles.hasRankWhitelist()) {
                            if (new OfflineRPlayer(e.getName(), e.getUniqueId()).getRank().has(ServerToggles.getRankRequired())) {
                                allowed = true;
                            } else {
                                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                                e.setKickMessage("You need " + ServerToggles.getRankRequired().getTag() + "to join this server!");
                                allowed = false;
                            }
                        }

                        if (allowed) {
                            e.allow();
                        }

                } else {
                    allowed = false;
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                    e.setKickMessage("Could not load player data! :(");
                }

            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.checkMovement = false;
        RPlayer pl = null;
        if (e.getPlayer().isOnline()) {
            RPlayerManager.getInstance().addPlayer(e.getPlayer(), new RPlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId()));
            pl = RPlayerManager.getInstance().getPlayer(e.getPlayer());
            pl.setLoaded(true);
        }

        if (pl.isVIP()) {
            e.setJoinMessage(null);
            RPStaff.sendVIPMessage(pl.getRankedName(false) + " §7joined " + GameManager.getGame().getPrimaryColor() + Bukkit.getServerName() + "§7.", true);
        } else {
            e.setJoinMessage("§2§l>> " + pl.getRankedName(false));
        }
    }

}
