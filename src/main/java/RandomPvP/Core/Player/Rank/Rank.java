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

    PLAYER(0, "PLAYER", "", ChatColor.BLUE),
    PREMIUM(1, "PREMIUM", "Premium ", ChatColor.AQUA),
    VIP(2, "VIP", "VIP ", ChatColor.YELLOW),
    BUILDER(3, "BUILDER", "Builder ", ChatColor.DARK_GREEN),
    MOD(4, "MOD", "MOD ", ChatColor.DARK_PURPLE),
    ADMIN(5, "ADMIN", "ADMIN ", ChatColor.DARK_RED);

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
}
