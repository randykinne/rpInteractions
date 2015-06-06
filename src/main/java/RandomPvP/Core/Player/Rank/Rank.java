package RandomPvP.Core.Player.Rank;

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

    PLAYER("PLAYER", "", ChatColor.BLUE),
    PRIME("PRIME", "PRIME ", ChatColor.GOLD),
    PREMIUM("PREMIUM", "PREMIUM ", ChatColor.AQUA),
    VIP("VIP", "VIP ", ChatColor.YELLOW),
    BUILDER("BUILDER", "BUILDER ",ChatColor.DARK_GREEN),
    MOD("MOD", "MOD ", ChatColor.DARK_PURPLE),
    ADMIN("ADMIN", "ADMIN ", ChatColor.RED),
    DEV("DEV", "DEV ", ChatColor.DARK_AQUA),
    OWNER("OWNER", "OWNER ", ChatColor.DARK_RED);

    private String name;
    private String rank;
    private ChatColor color;

    Rank(String name, String rank, ChatColor color) {
        this.name = name;
        this.rank = rank;
        this.color = color;
    }

    public String getFormattedName() {
        return (name.equals("PLAYER") ? "" : getTag());
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getTag() {
        return color + ChatColor.BOLD.toString() + getRank() + ChatColor.RESET;
    }

    public boolean has(Rank rank) {
        return (compareTo(rank) >= 0);
    }

    public Rank fromString(String name) {
        return valueOf(name) != null ? valueOf(name) : PLAYER;
    }
}
