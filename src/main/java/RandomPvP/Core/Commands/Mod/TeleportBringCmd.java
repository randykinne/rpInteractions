package RandomPvP.Core.Commands.Mod;

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
public class TeleportBringCmd {

    @Command(aliases = { "bring", "tphere" }, usage = "<player>", desc = "Teleports a player to your location", min = 1)
    public static void teleport(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.isStaff()) {
                if (Bukkit.getPlayer(args.getString(0)) != null) {
                    RPlayer target = RPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args.getString(0)));
                    target.getPlayer().teleport(pl.getPlayer().getLocation());
                    pl.message("§6§l>> §eYou have teleported " + target.getRankedName(false) + " §eto you.");
                    target.message("§6§l>> §eYou have been teleported to " + pl.getRankedName(false) + "§e.");
                }
            } else {
                throw new CommandException("§7You need to be §oMod §7to use this command.");
            }
        } else {
            sender.sendMessage("You must be in game to use this command!");
        }
    }
}
