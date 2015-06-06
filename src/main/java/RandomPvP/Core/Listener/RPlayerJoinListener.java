package RandomPvP.Core.Listener;


import RandomPvP.Core.Player.*;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Scoreboard.ScoreboardManager;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Server.General.Poll.PollManager;
import RandomPvP.Core.Server.General.ServerToggles;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.Server.Punish.PunishmentType;
import RandomPvP.Core.Util.Nametags.NametagManager;
import RandomPvP.Core.Util.NetworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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
public class RPlayerJoinListener implements Listener {

    private List<String> malTokens = Arrays.asList("`", "~", "*", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "[", "]", "-", "=", "+",
            "|", "{", "}", ";", ":", "'", ",", ".", "<", ">", "?", "/", " ");

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent e) {
        boolean allowed = true;
        String reason = "Kicked!";

        for(String charac : malTokens) {
            if(e.getName().contains(charac)) {
                allowed = false;
                reason = "We only allow real versions of Minecraft here, sorry.";
            }
        }

        if(allowed) {
            RPlayer pl = new RPlayer(e.getName(), e.getUniqueId(), true);
            // Making sure the player has a UUID
            if (pl.getUUID() != null) {
                //Checking if the server actually checks for bans or nah
                if (ServerToggles.checkForBan()) {
                    //If the player's banned
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

                //If the player's made it through the checks so far, we don't want to check these if the player is banned
                if (allowed) {
                    //If they purchased Premium, we will give it to them
                    JSONArray data;
                    {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://www.randompvp.com/api/m-shopping-purchases/m/28398956").openStream()));
                            StringBuilder sb = new StringBuilder();
                            {
                                String stored;
                                while ((stored = reader.readLine()) != null) {
                                    if (stored.startsWith("[")) {
                                        sb.append(stored);
                                    }
                                }
                            }

                            data = (JSONArray) new JSONParser().parse(sb.toString());
                        } catch (Exception ex) {
                            NetworkUtil.handleError(ex);
                            return;
                        }
                    }
                    for(int i=0; i < data.size(); i++) {
                        JSONObject jsonData = (JSONObject)data.get(i);
                        if(((JSONObject)((JSONArray) jsonData.get("items")).get(0)).get("item_name").equals("Premium Rank")) {
                            JSONObject userData = (JSONObject) jsonData.get("user");
                            if(userData != null && userData.get("username") != null) {
                                if (userData.get("username").equals(pl.getName()) && !pl.has(Rank.PREMIUM)) {
                                    pl.setRank(Rank.PREMIUM, true);
                                }
                            }
                        }
                    }

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
        }

        if (!allowed) {
            //Player didn't make it through the checks
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, reason);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final RPlayer pl = new RPlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId(), false);

        pl.setPlayer(e.getPlayer());
        PlayerManager.getInstance().addPlayer(pl);

        pl.message("\n");

        //Handle join messages
        if (pl.has(Rank.VIP)) {
            e.setJoinMessage(null); //proxy handles the join messages
        } else {
            e.setJoinMessage("§a§l>> " + pl.getRankedName(false) + " §9joined.");
        }

        //Handle scoreboard
        new BukkitRunnable() {
            public void run() {
                if(pl.getPlayer() != null && pl.isOnline()) {
                    ScoreboardManager.getInstance().updateScoreboard(pl);
                }
            }
        }.runTaskLater(RPICore.getInstance(), 30L);

        //Handle polls
        new BukkitRunnable() {
            public void run() {
                if(pl.getPlayer() != null && pl.isOnline()) {
                    if (ServerToggles.pollVotingEnabled()) {
                        PollManager manager = new PollManager();
                        RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
                        List<String> polls = manager.getOpenPolls();
                        for (String poll : polls) {
                            if (!manager.hasVoted(poll, pl)) {
                                manager.sendPollMessage(pl, poll);
                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskLater(RPICore.getInstance(), 70L);

        if(GameManager.getGame().allowIncognito()) {
            if (pl.getToggles().isIncognito()) {
                for(RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
                    if(!cur.has(Rank.VIP)) {
                        cur.getPlayer().hidePlayer(pl.getPlayer());
                    }
                }
                pl.getToggles().setInvisForIncognito(true);

                new BukkitRunnable() {
                    public void run() {
                        pl.message(MsgType.NETWORK, "You are currently in " + ChatColor.BLUE.toString() + ChatColor.BOLD + "INCOGNITO MODE" + ChatColor.GRAY + ". " +
                                "You'll be hidden for all players that aren't staff until you speak.");
                        pl.playSound(pl.getLocation(), Sound.CLICK, 1F, 1F);
                    }
                }.runTaskLater(RPICore.getInstance(), 45L);
            }

            for (RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
                if (cur.getToggles().isInvisForIncognito()) {
                    if (!pl.has(Rank.VIP)) {
                        pl.getPlayer().hidePlayer(cur.getPlayer());
                    }
                }
            }
        }

        //Handle nametags above their head
        new BukkitRunnable() {
            public void run() {
                NametagManager.sendTeamsToPlayer(pl.getPlayer());
                NametagManager.clear(pl.getName());
                pl.updateNametag();
            }
        }.runTaskLater(RPICore.getInstance(), 1L);
    }

}
