package RandomPvP.Core.Player.Rank;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.command.CommandSender;
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
public class Perms {

    public static boolean hasPerms(RPlayer pl, Rank req) {
        return pl.getRank().has(req);
    }

    public static boolean hasPerms(CommandSender sender, Rank req) {
        return !(sender instanceof Player) || PlayerManager.getInstance().getPlayer((Player) sender).getRank().has(req);
    }
}
