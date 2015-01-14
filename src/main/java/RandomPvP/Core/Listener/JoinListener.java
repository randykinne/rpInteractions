package RandomPvP.Core.Listener;


import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.*;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
        boolean allowed = true;
        String reason = "Kicked!";

        RPlayer pl = new RPlayer(e.getName(), e.getUniqueId());
        // Making sure the player has a UUID
        if (pl.getUUID() != null) {
            //Checking if the current gamestate allows players joining the server
            if (GameManager.getState().isJoinable()) {
                //Checking the number of online players vs max players of the game, if greater then check overflow
                if (RPlayerManager.getInstance().getOnlinePlayers().size() >= GameManager.getGame().getMaxPlayers() && GameManager.getGame().getMaxPlayers() != -1) {
                    //Players must be Premium to use the overflow
                    if (pl.isDonator()) {
                        //The sum of the online players and the overflow is less than the sum of the max players and overflow
                        //If true, allow because the overflow has room
                        if (RPlayerManager.getInstance().getOnlinePlayers().size() + GameManager.getGame().getBufferOverflow()
                                < GameManager.getGame().getMaxPlayers() + GameManager.getGame().getBufferOverflow() || GameManager.getGame().getBufferOverflow() == -1) {
                            allowed = true;
                        } else {
                            allowed = false;
                            reason = "Server overflow full!";
                        }
                    } else {
                        allowed = false;
                        reason = "Server full! Purchase " + Rank.PREMIUM.getTag() + "§fto join the overflow on full servers!";
                    }
                } else {
                    allowed = true;
                }

            } else {
                allowed = false;
                reason = "You may not join during " + GameManager.getState().getName() + "!";
            }

            //If the server has a rank whitelist in the first place
            if (ServerToggles.hasRankWhitelist()) {
                //If it does, check if the player has the rank needed to join
                if (!pl.getRank().has(ServerToggles.getRankRequired())) {
                    allowed = false;
                    reason = "You must have " + ServerToggles.getRankRequired().getTag() + "§fto join this server!";
                }
            }

        } else {
            //If the player doesn't have a UUID
            allowed = false;
            reason = "Failed to download player data, please retry in a few minutes.";
        }

        if (!allowed) {
            //Player didn't make it through the checks
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, reason);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //Prevents "player moved too quickly!" related spam in console, also better for the player
        ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.checkMovement = false;

        RPlayer pl = new RPlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId());

        //Avoiding dem NPEs
        if (e.getPlayer().isOnline()) {
            pl.setPlayer(e.getPlayer());
            //setLoaded() used for something but I can't remember
            pl.setLoaded(true);
            RPlayerManager.getInstance().addPlayer(pl);
        }

        //Handle join messages
        if (pl.isVIP()) {
            e.setJoinMessage(null);
            RPStaff.sendVIPMessage(pl.getRankedName(false) + " §7joined " + GameManager.getGame().getPrimaryColor() + Bukkit.getServerName() + "§7.", true);
        } else {
            e.setJoinMessage("§2§l>> " + pl.getRankedName(false));
        }
    }

}
