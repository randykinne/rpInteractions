package RandomPvP.Core.Game.Team;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Game.Team.Exception.TeamException;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
public class TeamManager {

    protected static final Map<String, Team> teams = new HashMap<>();
    protected static final SetMultimap<Team.Type, Team> teamsByType = HashMultimap.create();

    protected static final SetMultimap<Team.Type, RPlayer> playersByType = HashMultimap.create();
    protected static final SetMultimap<Team, RPlayer> playersByTeam = HashMultimap.create();



    public static Map<String, Team> getTeams() {
        return teams;
    }

    public static Team getTeam(String name) {
        if (teams.containsKey(name)) {
            return teams.get(name);
        }
        return null;
    }

    public static Set<Team> getParticipatingTeams() {
        return teamsByType.get(Team.Type.Participating);
    }
    public static Set<Team> getObservingTeams() {
        return teamsByType.get(Team.Type.Observing);
    }

    public static Set<RPlayer> getParticipatingPlayers() {
        return playersByType.get(Team.Type.Participating);
    }
    public static Set<RPlayer> getObservingPlayers() {
        return playersByType.get(Team.Type.Observing);
    }

    public static Set<RPlayer>  getPlayers(Team team) {
        return playersByTeam.get(team);
    }

    public static void registerTeam(Team team) {
        if (!teams.containsValue(team)) {
            teams.put(team.getName(), team);
            teamsByType.put(team.getType(), team);
            System.out.println("Registered team: " + team.getName());
        }
    }

    public static void unregisterAllTeams() {
        for (Team team : getTeams().values()) {
            getTeams().values().remove(team);
        }
        System.out.println("All teams have been unregistered");
    }


    public static void leaveTeam(RPlayer player, Team team) {
        if (GameManager.getGame().isTeamBased()) {
            if (player.getTeam().getName().equalsIgnoreCase(team.getName())) {
                team.removePlayer(player);
                playersByTeam.remove(team, player);
                playersByType.remove(team.getType(), player);
            }
        }
    }

    public static void joinTeam(RPlayer player, Team team) throws TeamException {
        if (GameManager.getGame().isTeamBased()) {
            if (player.getTeam() != null) {
                leaveTeam(player, player.getTeam());
            }

            if (team.getMaxSize() == -1 || team.getPlayers().size() < team.getMaxSize()) {
                team.addPlayer(player);
                player.message("§3§l>> §7You have joined " + team.getColor() + team.getName() + "§7.");
                /*
                if (team.isHidden() && !RPICore.list.isVisible(player.getPlayer())) {
                    RPICore.list.hidePlayer(player.getPlayer());
                } else {
                    RPICore.list.showPlayer(player.getPlayer());
                }
                */

                for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                    if (team.getType() == Team.Type.Observing) {
                        pl.getPlayer().hidePlayer(player.getPlayer());
                    } else {
                        pl.getPlayer().showPlayer(player.getPlayer());
                    }
                }
            } else {
                throw new TeamException(player, team.getColor() + team.getName() + "§7 is full!");
            }
        } else {
            throw new TeamException(player, "Teams are not enabled!");
        }
    }
}
