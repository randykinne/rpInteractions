package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.ServerToggles;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
public class SetWhitelistCmd {

    @Command(aliases = "setwhitelist", desc = "Sets the rank whitelist level", usage = "<rank> -", min = 1)
    public static void setwhitelist(final CommandContext args, CommandSender sender) throws CommandException {
        RPlayer pl = null;
        if (sender instanceof Player) {
            pl = RPlayerManager.getInstance().getPlayer((Player) sender);

            if (!pl.isStaff()) {
                throw new CommandException("You need Mod to use this command.");
            } else {
                if (Rank.valueOf(args.getString(0).toUpperCase()) != null) {
                    Rank rank = Rank.valueOf(args.getString(0).toUpperCase());

                    ServerToggles.setRankRequired(rank);
                    Bukkit.broadcastMessage("§5§l>> " + pl.getRankedName(false) + " §eset the minimum rank to " + rank.getTag().replace(" ", "") + "§e.");
                } else {
                    throw new CommandException("Rank not found! Available: " + String.valueOf(Arrays.asList(Rank.values())).replace("[", "").replace("]", ""));
                }
            }

        } else {
            sender.sendMessage("You need to be a player!");
        }
    }
}
