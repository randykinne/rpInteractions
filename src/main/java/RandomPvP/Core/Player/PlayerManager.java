package RandomPvP.Core.Player;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.NetworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
    private ConcurrentHashMap<UUID, RPlayer> players = new ConcurrentHashMap<>();

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public void getInMap() {
        if (players.values().size() > 0) {
            StringBuilder s = new StringBuilder("");
            for (RPlayer pl : players.values()) {
                s.append(pl.getName() + ", ");
            }

            System.out.println(s.toString());
        } else {
            System.out.println("There are no players in map!");
        }

    }

    public RPlayer getConsole() {
        return new RPlayer("CONSOLE", null, false);
    }

    public void addPlayer(RPlayer pl) {
        if (pl != null) {
            players.putIfAbsent(pl.getUUID(), pl);
        }
    }

    public RPlayer getPlayer(Player p) {
        if (RPICore.debugEnabled) {
            getInMap();
        }
        for (UUID id : players.keySet()) {
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

        for (UUID n : players.keySet()) {
            if (name.equalsIgnoreCase(Bukkit.getPlayer(n).getName())) {
                return players.get(n);
            }
        }

        return null;
    }

    public RPlayer getPlayer(UUID id) {
        if (RPICore.debugEnabled) {
            getInMap();
        }

        for (UUID od: players.keySet()) {
            if (od == id) {
                return players.get(id);
            }
        }
        RPlayer player = null;

        if (Bukkit.getPlayer(id) != null) {
            player = getPlayer(Bukkit.getPlayer(id).getName());
        }

        return null;
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
        RPlayer value = getPl(p.getName());
        if (value != null) {
            removePlayerFromDatabase(value);
            for (Iterator<UUID> i = players.keySet().iterator(); i.hasNext();) {
                UUID next = i.next();
                if (next.equals(p.getUniqueId())) {
                    i.remove();
                }
            }
        }
    }

    private RPlayer getPl(String name) {
        for (Iterator<UUID> i = players.keySet().iterator(); i.hasNext();) {
            UUID next = i.next();
            if (Bukkit.getPlayer(next).getName().equalsIgnoreCase(name)) {
                return players.get(next);
            }
        }

        return null;
    }


    private void removePlayerFromDatabase(final RPlayer pl) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("DELETE FROM `online_players` WHERE `id` = ?;");
                    stmt.setInt(1, pl.getRPID());
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.run();
    }

    public Collection<RPlayer> getOnlinePlayers() {
        Collection<RPlayer> online = new ArrayList<>();
        if (players.values().size() != 0) {
            for (RPlayer pl : players.values()) {
                if (pl != null) {
                    if (pl.getPlayer() != null) {
                        online.add(pl);
                    }  else {
                        removePlayer(pl.getPlayer());
                    }
                }
            }
        }
        return online;
    }

    public RPlayer getRandomPlayer() {
        List<RPlayer> online = new ArrayList<RPlayer>();
        {
            for(RPlayer pl : getOnlinePlayers()) {
                online.add(pl);
            }
        }

        if(online.size() > 0) {
            return online.get(new Random().nextInt(getOnlinePlayers().size()));
        } else {
            return null;
        }
    }
}
