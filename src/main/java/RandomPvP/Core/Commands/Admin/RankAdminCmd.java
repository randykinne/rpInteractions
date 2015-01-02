package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.RPICore;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
public class RankAdminCmd {

    @Command(aliases = { "perm", "rank" }, usage = "[set/unset] [player] [rank] - Sets a player's rank", desc = "Sets a player's rank", min = 1, max = 3)
    public static void rank(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.getRank().has(Rank.ADMIN)) {
                if (Bukkit.getPlayer(args.getString(1)) != null) {
                    RPlayer target = RPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args.getString(1)));
                    if (args.getString(0).equalsIgnoreCase("set")) {
                        if (Rank.valueOf(args.getString(2).toUpperCase()) != null) {
                            target.setRank(Rank.valueOf(args.getString(2).toUpperCase()));
                            target.message("§6§l>> §eYour rank has been set to " + target.getRank().getName() + "§e.");
                            pl.message("§6§l>> §eSet " + target.getRankedName(false) + "§e to " + target.getRank().getName());
                        } else {
                            throw new CommandException("Available ranks: " + String.valueOf(Arrays.asList(Rank.values())).replace("[", "").replace("]", ""));
                        }
                    } else if (args.getString(0).equalsIgnoreCase("unset")) {
                        target.setRank(Rank.PLAYER);
                        target.message("§6§l>> §eYour rank has been set to " + target.getRank().getName() + "§e.");
                        pl.message("§6§l>> §eUnset " + target.getRankedName(false) + " §e's rank.");
                    } else {
                        throw new CommandUsageException("Incorrect usage", "Arg 1 can only have 2 values [set] or [unset]");
                    }
                } else {

                    if (args.getString(1).equalsIgnoreCase("set")) {
                        if (Rank.valueOf(args.getString(2).toUpperCase()) != null) {
                            final Rank rank = Rank.valueOf(args.getString(2));
                            pl.message("§6§l>> §eSet " + args.getString(0) + " §e to " + rank.getTag());
                            new BukkitRunnable() {
                                public void run() {
                                    OfflineRPlayer target =  new OfflineRPlayer(args.getString(0), UUIDCache.getUUID(args.getString(0)));
                                    target.setRank(rank);
                                }
                            }.runTaskAsynchronously(RPICore.getInstance());

                        }
                    }
                }
            } else {
                throw new CommandException("§7You need to be §oAdmin §7to use this command.");
            }
        }
    }
}
