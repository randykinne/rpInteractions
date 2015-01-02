package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
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
public class WhereAmICommand {

    @Command(aliases = { "whereami", "where", "server" }, desc = "Shows your current game/location on the network.", usage = "- Shows your current game/location on the network", max = 0, min = 0)
    public static void whereami(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            pl.message("§8§l>> §7You are playing " + GameManager.getGame().getPrimaryColor() + GameManager.getGame().getName() + "§7 on " + GameManager.getGame().getPrimaryColor() + Bukkit.getServerName() + "§7.");
        } else {
            throw new CommandException("You must be a player to be on the server!");
        }
    }
}
