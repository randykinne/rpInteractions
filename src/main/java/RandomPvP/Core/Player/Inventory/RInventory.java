package RandomPvP.Core.Player.Inventory;

import RandomPvP.Core.Player.RPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
public abstract class RInventory implements RPInventory {

    public RInventory(int size, String type, String name) {
        Bukkit.createInventory(null, 54, "§8§l" + type.toUpperCase() + " §0" + name);
    }

    public void open(Player player) {
        player.openInventory(this);
    }

    public void open(RPlayer player) {
        open(player.getPlayer());
    }
}
