package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.GameMode;
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
public class GamemodeCmd {

    @Command(aliases = { "gm", "gamemode" }, desc = "Changes gamemode", usage = "[#/gamemode]", min = 1)
    public static void gamemode(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.getRank().has(Rank.BUILDER)) {
                if (args.getString(0).equalsIgnoreCase("0") || args.getString(0).equalsIgnoreCase("survival") || args.getString(0).equalsIgnoreCase("s")) {
                    pl.getPlayer().setGameMode(GameMode.SURVIVAL);
                } else if (args.getString(0).equalsIgnoreCase("1") || args.getString(0).equalsIgnoreCase("creative") || args.getString(0).equalsIgnoreCase("c")) {
                    pl.getPlayer().setGameMode(GameMode.CREATIVE);
                } else if (args.getString(0).equalsIgnoreCase("2") || args.getString(0).equalsIgnoreCase("adventure") || args.getString(0).equalsIgnoreCase("a")) {
                    pl.getPlayer().setGameMode(GameMode.ADVENTURE);
                } else {
                    throw new CommandException("Gamemode not found!");
                }

                pl.message("§6§l>> §7Gamemode switched to §e" + pl.getPlayer().getGameMode().name() + "§7.");
            } else {
                throw new CommandException("§7You need to be §oBuilder §7to use this command.");
            }
        } else {
            sender.sendMessage("You must be a player to use this command");
        }
    }
}
