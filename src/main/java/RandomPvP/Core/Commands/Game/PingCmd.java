package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.RPICore;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
public class PingCmd {

    private static ChatColor getColor(int ping) {
        if (ping <= 125) {
            return ChatColor.GREEN;
        } else if (ping > 125 && ping <= 250) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.RED;
        }
    }

    @Command(aliases = { "ping", "pong" }, desc = "Shows your ping to the server", usage = "-")
    public static void ping(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (args.argsLength() > 0) {
                if (Bukkit.getPlayer(args.getString(0)) != null) {

                    try {
                        int ping = RPICore.getInstance().list.getPlayerPing(Bukkit.getPlayer(args.getString(0)));
                        sender.sendMessage("§9§l>> " +
                                RPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args.getString(0))).getRankedName(false) +
                                "§b's current ping is " +
                                getColor(ping) +
                                ping +
                        "ms");
                    } catch (IllegalAccessException e) {
                        throw new CommandException("Unable to retrieve player's ping. Ping OP! :(");
                    }
                } else {
                    throw new CommandException(args.getString(0) + " is not online! Did you spell it correctly?");
                }
            } else {
                try {
                    int ping = RPICore.getInstance().list.getPlayerPing((Player) sender);
                    sender.sendMessage("§9§l>> §bYour current ping is " + getColor(ping) + ping + "ms");
                } catch (IllegalAccessException e) {
                    throw new CommandException("Unable to retrieve your ping. Ping OP! :(");
                }
            }
        } else {
            throw new CommandException("You must be a player to use this command!");
        }
    }
}
