package RandomPvP.Core.Player;

import RandomPvP.Core.Server.Game.GameManager;
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
public enum MsgType {

    CREDIT("§2§L>> §a", ChatColor.GREEN),
    INFO("§8§l>> §7", ChatColor.GRAY),
    GAME(GameManager.getGame().getPrimaryColor() + "§l>> §7", GameManager.getGame().getPrimaryColor()),
    NETWORK("§9§l>> §7", ChatColor.AQUA),
    ERROR("§4§l>> §7", ChatColor.RED);

    private String prefix;
    private ChatColor color;

    MsgType(String prefix, ChatColor color) {
        this.prefix = prefix;
        this.color = color;
    }

    public String getPrefix() {
        return prefix;
    }

    public ChatColor getColor() {
        return color;
    }

}