package RandomPvP.Core.Game.Team;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
public class Team {

    String name;
    String teamName;
    ChatColor color;
    Type type = Type.Participating;
    int maxSize = -1;
    boolean showInChat = false;
    boolean hidden = false;
    boolean allowFriendlyFire = false;
    boolean canSeeFriendlyInvisibles = false;
    boolean teamDisplayNameColor = true;

    private Map<String, RPlayer> members = new HashMap<>();
    public void addPlayer(RPlayer player) {
        if (!members.containsValue(player)) {
            members.put(player.getName(), player);
            player.setTeam(this);
        }
    }

    public void removePlayer(RPlayer player) {
        if (members.containsValue(player)) {
            members.remove(player.getName());
            player.setTeam(null);
        }
    }

    public Collection<RPlayer> getPlayers() { return members.values(); }



    public Team(String name, String teamName, ChatColor color) {
        this.name = name;
        this.teamName = teamName;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public String getTeamName() {
        return teamName;
    }
    public ChatColor getColor() {
        return color;
    }

    public boolean isShownInChat() {
        return showInChat;
    }
    public void setShowInChat(boolean showInChat) {
        this.showInChat = showInChat;
    }

    public boolean setsTeamDisplayNameColor() { return teamDisplayNameColor; }
    public void setTeamUsesDisplayNameColor(boolean color) { this.teamDisplayNameColor = color;}

    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setType(Type type) {
        this.type = type;
    }
    public Type getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }
    public void setMaxsize(int size) {
        this.maxSize = size;
    }

    public void register() {
        TeamManager.registerTeam(this);
    }

    public enum Type {
        /** Team type that can interact with the map during a match and
         * participate in the action. */
        Participating,

        /** Team type that cannot interact with the map at any time and can
         * only watch the match in progress.
         */
        Observing
    }
}
