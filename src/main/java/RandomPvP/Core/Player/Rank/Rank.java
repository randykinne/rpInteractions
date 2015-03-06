package RandomPvP.Core.Player.Rank;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

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
public enum Rank {

    PLAYER(0, "PLAYER", "", ChatColor.BLUE),
    PRIME(1, "PRIME", "PRIME ", ChatColor.GOLD),
    PREMIUM(2, "PREMIUM", "PREMIUM ", ChatColor.AQUA),
    VIP(3, "VIP", "VIP ", ChatColor.YELLOW),
    BUILDER(4, "BUILDER", "BUILDER ", ChatColor.DARK_GREEN),
    MOD(5, "MOD", "MOD ", ChatColor.DARK_PURPLE),
    ADMIN(6, "ADMIN", "ADMIN ", ChatColor.RED),
    OWNER(7, "OWNER", "OWNER ", ChatColor.DARK_RED);

    String rank;
    String name;
    ChatColor color;
    int weight;

    Rank(int weight, String rank, String name, ChatColor color) {
        this.rank = rank;
        this.name = name;
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public String getFormattedName() { return getName().toUpperCase(); }

    public String getRank() { return rank; }

    public String getName() {
        return color + rank;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getTag() {
            return color + ChatColor.BOLD.toString() + name + ChatColor.RESET;
    }

    public boolean has(Rank rank) {
        return (compareTo(rank) >= 0);
    }

    public Rank fromString(String name) {
        return valueOf(name) != null ? valueOf(name) : PLAYER;
    }
}
