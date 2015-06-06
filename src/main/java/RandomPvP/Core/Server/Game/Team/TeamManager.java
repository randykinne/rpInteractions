package RandomPvP.Core.Server.Game.Team;

import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Nametags.NametagAPI;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.bukkit.ChatColor;

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

    private static final Map<String, Team> teams = new HashMap<>();

    public static Map<String, Team> getTeams() {
        return teams;
    }

    public static Team getTeam(String name) {
        if (teams.containsKey(name)) {
            return teams.get(name);
        }
        return null;
    }

    public static void registerTeam(Team team) {
        if (!teams.containsValue(team)) {
            teams.put(team.getName(), team);
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
                player.updateNametag();
            }
        }
    }

    public static boolean joinTeam(RPlayer player, Team team){
        boolean success = false;
        {
            if (GameManager.getGame().isTeamBased()) {
                if (player.getTeam() != null) {
                    leaveTeam(player, player.getTeam());
                }

                if (team.getMaxSize() == -1 || team.getPlayers().size() < team.getMaxSize()) {
                    team.addPlayer(player);

                    player.message("§3§l>> §7You have joined " + team.getColor() + team.getName() + "§7.");
                    player.getPlayer().spigot().setCollidesWithEntities(!(team.getType() == Team.Type.Observing));
                    player.updateNametag();

                    for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                        if (team.getType() == Team.Type.Observing) {
                            pl.getPlayer().hidePlayer(player.getPlayer());
                        } else {
                            pl.getPlayer().showPlayer(player.getPlayer());
                        }
                    }

                    success = true;
                } else {
                    player.message(MsgType.ERROR, team.getColor() + team.getName() + "§7 is full!");
                }
            } else {
                player.message(MsgType.ERROR, "Teams are not enabled for " + GameManager.getGame().getPrimaryColor() + GameManager.getGame().getName() + ChatColor.GRAY + ".");
            }
        }
        return success;
    }
}
