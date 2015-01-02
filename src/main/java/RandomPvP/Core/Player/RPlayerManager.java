package RandomPvP.Core.Player;

import org.bukkit.entity.Player;

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
public class RPlayerManager {

    private static RPlayerManager instance;
    ConcurrentHashMap<Player, RPlayer> players = new ConcurrentHashMap<>();

    public static RPlayerManager getInstance() {
        if (instance == null) {
            instance = new RPlayerManager();
        }
        return instance;
    }

    public void addPlayer(Player p, RPlayer pl) {
        if (p != null) {
            pl.setPlayer(p);
            players.putIfAbsent(p, pl);
        }
    }

    public RPlayer getPlayer(Player p) {
        RPlayer player = null;
        for (Player pl: players.keySet()) {
            if (pl.getUniqueId() == p.getUniqueId()) {
                player = players.get(pl);
            }
        }

        return player;
    }

    public void removePlayer(Player p) {
        final RPlayer value = players.get(p);
        if (value != null) {
            players.remove(p);
        }
    }

    public void removePlayer(RPlayer pl) {
        if (getPlayer(pl.getPlayer()) != null) {
            players.remove(pl);
        }

    }

    public Collection<RPlayer> getOnlinePlayers() {
        Collection<RPlayer> online = new ArrayList<>();
        if (players.values().size() != 0) {
            for (RPlayer pl : players.values()) {
                if (pl != null) {
                    if (pl.getPlayer().isOnline()) {
                        online.add(pl);
                    } else {
                        removePlayer(pl);
                    }
                }
            }
        }
        return online;
    }
}
