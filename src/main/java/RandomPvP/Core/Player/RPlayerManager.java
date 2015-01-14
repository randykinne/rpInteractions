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
    ConcurrentHashMap<UUID, RPlayer> players = new ConcurrentHashMap<>();

    public static RPlayerManager getInstance() {
        if (instance == null) {
            instance = new RPlayerManager();
        }
        return instance;
    }

    public void addPlayer(RPlayer pl) {
        if (pl != null) {
            players.putIfAbsent(pl.getUUID(), pl);
        }
    }

    public RPlayer getPlayer(Player p) {
        RPlayer player = null;

        for (UUID id: players.keySet()) {
            if (id == p.getUniqueId()) {
                player = players.get(id);
            }
        }

        return player;
    }

    public RPlayer getPlayer(UUID id) {
        RPlayer player = null;

        for (UUID od: players.keySet()) {
            if (od == id) {
                player = players.get(id);
            }
        }

        return player;
    }

    public void removePlayer(Player p) {
        final RPlayer value = players.get(p.getUniqueId());
        if (value != null) {
            players.remove(p.getUniqueId());
        }
    }

    public void removePlayer(RPlayer pl) {
        RPlayer value = players.get(pl.getUUID());
        if (value != null) {
            players.remove(pl.getUUID());
        }
    }

    public Collection<RPlayer> getOnlinePlayers() {
        Collection<RPlayer> online = new ArrayList<>();
        if (players.values().size() != 0) {
            for (RPlayer pl : players.values()) {
                if (pl != null) {
                    if (pl.getPlayer() != null) {
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
