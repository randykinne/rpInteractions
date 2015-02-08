package RandomPvP.Core.Listener;


import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.*;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Punish.PunishmentType;
import RandomPvP.Core.RPICore;
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
public class RPlayerJoinListener implements Listener {

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent e) {
        boolean allowed = true;
        String reason = "Kicked!";

        RPlayer pl = new RPlayer(e.getName(), e.getUniqueId(), true);
        // Making sure the player has a UUID
        if (pl.getUUID() != null) {
            //Checking if the server actually checks for bans or nah
            if (ServerToggles.checkForBan()) {
                //If the player's banned
                if (RPICore.pEnabled) {
                    if (pl.isBanned()) {
                        Punishment pm = PunishmentManager.getInstance().getActiveBan(pl.getUUID());
                        if (pm.getType() == PunishmentType.TEMPORARY_BAN) {
                            if ((pm.getEnd() - System.currentTimeMillis()) >= 0L) {
                                pm.setActive(false);
                                pm.save();
                            } else {
                                allowed = false;
                                reason = PunishmentManager.getInstance().generateMessage(pm);
                            }
                        } else {
                            allowed = false;
                            reason = PunishmentManager.getInstance().generateMessage(pm);
                        }
                    }

                }
            }

            //If the player's made it through the checks so far, we don't want to check these if the player is banned
            if (allowed) {
                //Checking if the current gamestate allows players joining the server
                if (GameManager.getState().isJoinable()) {
                    //Trying to prevent an NPE when there is no players online? Not sure of the exact issue quite yet
                    if (PlayerManager.getInstance().getOnlinePlayers() != null) {
                        //Checking the number of online players vs max players of the game, if greater then check overflow
                        if (PlayerManager.getInstance().getOnlinePlayers().size() >= GameManager.getGame().getMaxPlayers() && GameManager.getGame().getMaxPlayers() != -1) {
                            //Players must be Premium to use the overflow
                            if (pl.has(Rank.PREMIUM)) {
                                //The sum of the online players and the overflow is less than the sum of the max players and overflow
                                //If true, allow because the overflow has room
                                if (PlayerManager.getInstance().getOnlinePlayers().size() + GameManager.getGame().getBufferOverflow()
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
            }
        } else {
            //If the player doesn't have a UUID, somehow
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

        //Player made it through the checks in the method above, so they're connecting
        RPlayer pl = new RPlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId(), false);

        //Avoiding dem NPEs
        if (e.getPlayer() != null) {
            pl.setPlayer(e.getPlayer());
            PlayerManager.getInstance().addPlayer(pl, e.getPlayer().getUniqueId());
        }

        pl.message("\n");

        //Handle join messages
        if (pl.has(Rank.VIP)) {
            e.setJoinMessage(null);
            Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(false) + " §9joined §l" + Bukkit.getServerName() + "§9.");
        } else {
            e.setJoinMessage("§a§l>> " + pl.getRankedName(false) + " §9joined.");
        }
    }

}
