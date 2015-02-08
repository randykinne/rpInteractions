package RandomPvP.Core.Player;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
public class PlayerManager {

    private static PlayerManager instance;
    ConcurrentHashMap<UUID, RPlayer> players = new ConcurrentHashMap<>();

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public void getInMap() {
        if (players.values().size() > 0) {
            for (RPlayer pl : players.values()) {
                System.out.println(pl.getName());
            }
        } else {
            System.out.println("There are no players in map!");
        }
    }

    public void addPlayer(RPlayer pl, UUID id) {
        if (pl != null) {
            players.putIfAbsent(id, pl);
        }
    }

    public RPlayer getPlayer(Player p) {
        if (RPICore.debugEnabled) {
            getInMap();
        }
        for (UUID id: players.keySet()) {
            if (id == p.getUniqueId()) {
                return players.get(id);
            }
        }

        return null;
    }

    public RPlayer getPlayer(String name) {
        if (RPICore.debugEnabled) {
            getInMap();
        }
        if (Bukkit.getPlayer(name) != null) {
            return getPlayer(Bukkit.getPlayer(name));
        }
        return null;
    }

    public RPlayer getPlayer(UUID id) {
        if (RPICore.debugEnabled) {
            getInMap();
        }
        RPlayer player = null;

        for (UUID od: players.keySet()) {
            if (od == id) {
                player = players.get(id);
            }
        }

        return player;
    }

    public RPlayer getPlayer(int rpid) {
        if (RPICore.debugEnabled) {
            getInMap();
        }
        for (RPlayer pl : players.values()) {
            if (pl.getRPID() == rpid) {
                return players.get(pl.getUUID());
            }
        }

        return null;
    }

    public void removePlayer(Player p) {
        RPlayer value = players.get(p.getUniqueId());
        if (value != null) {
            removePlayerFromDatabase(value);
            players.remove(p.getUniqueId());
        }
    }



    private void removePlayerFromDatabase(final RPlayer pl) {
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("DELETE FROM `online_players` WHERE `id` = ?;");
                    stmt.setInt(1, pl.getRPID());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public Collection<RPlayer> getOnlinePlayers() {
        Collection<RPlayer> online = new ArrayList<>();
        if (players.values().size() != 0) {
            for (RPlayer pl : players.values()) {
                if (pl != null) {
                    if (pl.getPlayer() != null && pl.getPlayer().isOnline()) {
                        online.add(pl);
                    } else {
                        removePlayer(pl.getPlayer());
                    }
                }
            }
        }
        return online;
    }

    public RPlayer getRandomPlayer() {
        RPlayer[] online = (RPlayer[]) getOnlinePlayers().toArray();
        Random rand = new Random();
        return online[rand.nextInt(online.length)];
    }
}
